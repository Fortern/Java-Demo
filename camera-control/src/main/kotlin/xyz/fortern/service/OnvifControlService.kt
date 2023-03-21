package xyz.fortern.service

import be.teletask.onvif.OnvifManager
import be.teletask.onvif.listeners.OnvifResponseListener
import be.teletask.onvif.models.*
import be.teletask.onvif.responses.OnvifResponse
import org.apache.http.auth.UsernamePasswordCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import xyz.fortern.constant.ONVIF_INFO_CACHE
import xyz.fortern.constant.ONVIF_PROFILE_CACHE
import xyz.fortern.constant.ONVIF_SNAPSHOT_CACHE
import xyz.fortern.exception.OnvifResponseTimeoutException
import xyz.fortern.pojo.Camera
import xyz.fortern.pojo.Preset
import xyz.fortern.pojo.PtzInfo
import xyz.fortern.util.*
import java.io.InputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * Onvif设备控制
 */
@Service
class OnvifControlService {
	private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
	
	@Lazy
	@Autowired
	private lateinit var onvifControlService: OnvifControlService
	
	/**
	 * 一个Onvif管理器，在里面注册一个Onvif响应监听器
	 */
	private val onvifManager = OnvifManager(object : OnvifResponseListener {
		override fun onResponse(onvifDevice: OnvifDevice, onvifResponse: OnvifResponse<*>) {
			logger.info("PTZ response: {}", onvifResponse.xml)
		}
		
		override fun onError(onvifDevice: OnvifDevice, errorCode: Int, errorMsg: String) {
			logger.warn("PTZ response err [{}]: {} ", errorCode, errorMsg)
		}
	})
	
	/**
	 * 获取设备信息
	 *
	 * @param camera 摄像头信息
	 */
	@Cacheable(cacheNames = [ONVIF_INFO_CACHE], key = "#camera.id")
	fun getOnvifInfo(@NonNull camera: Camera): OnvifDeviceInformation {
		var info: OnvifDeviceInformation? = null
		val lock = MyLock()
		onvifManager.getDeviceInformation(
			OnvifDevice(
				camera.ip + ":" + camera.port,
				camera.username,
				camera.password
			)
		) { _, deviceInformation ->
			info = deviceInformation
			lock.aSignal()
		}
		lock.aWait(8, TimeUnit.SECONDS)
		
		if (info == null) {
			throw OnvifResponseTimeoutException()
		}
		return info!!
	}
	
	/**
	 * 获取MediaProfiles
	 *
	 * @param camera Onvif设备
	 * @throws OnvifResponseTimeoutException 请求Onvif设备信息超时
	 */
	@Cacheable(value = [ONVIF_PROFILE_CACHE], key = "#camera.id")
	fun getOnvifMediaProfiles(@NonNull camera: Camera): List<OnvifMediaProfile> {
		var mediaProfiles: List<OnvifMediaProfile>? = null
		val mLock = MyLock()
		onvifManager.getMediaProfiles(camera.toDevice()) { _, mediaProfilesReceived ->
			mediaProfiles = mediaProfilesReceived
			mLock.aSignal()
		}
		mLock.aWait(8, TimeUnit.SECONDS)
		
		if (mediaProfiles == null) {
			throw OnvifResponseTimeoutException()
		}
		return mediaProfiles!!
	}
	
	/**
	 * 获取配置信息
	 */
	fun getConfigurationList(@NonNull camera: Camera): List<OnvifConfiguration> {
		var configurationList: List<OnvifConfiguration>? = null
		val mLock = MyLock()
		onvifManager.getConfigurations(
			OnvifDevice(camera.ip + ":" + camera.port, camera.username, camera.password)
		) { _, configurations ->
			configurationList = configurations
			mLock.aSignal()
		}
		mLock.aWait(8, TimeUnit.SECONDS)
		if (configurationList == null) {
			throw OnvifResponseTimeoutException()
		}
		return configurationList!!
	}
	
	/**
	 * 获取配置信息
	 */
	fun getService(@NonNull camera: Camera): OnvifServices {
		var onvifServices: OnvifServices? = null
		val mLock = MyLock()
		onvifManager.getServices(
			OnvifDevice(camera.ip + ":" + camera.port, camera.username, camera.password)
		) { _, result ->
			onvifServices = result
			mLock.aSignal()
		}
		mLock.aWait(8, TimeUnit.SECONDS)
		if (onvifServices == null) {
			throw OnvifResponseTimeoutException()
		}
		return onvifServices!!
	}
	
	/**
	 * 获取抓图链接
	 */
	@Cacheable(
		value = [ONVIF_SNAPSHOT_CACHE], key = "#camera.id",
		condition = "@onvifControlService.getOnvifInfo(#camera).manufacturer.equals('HIKVISION')"
	)
	fun getSnapshotUri(@NonNull camera: Camera): String {
		var uri: String? = null
		val lock = MyLock()
		onvifManager.getSnapshotURI(
			camera.toDevice(),
			onvifControlService.getOnvifMediaProfiles(camera)[0]
		) { _, _, s ->
			uri = s
			lock.aSignal()
		}
		lock.aWait(8, TimeUnit.SECONDS)
		if (uri == null) {
			throw OnvifResponseTimeoutException()
		}
		return uri!!
	}
	
	/**
	 * 抓图
	 */
	fun capture(@NonNull camera: Camera): InputStream? {
		val snapshotUri = onvifControlService.getSnapshotUri(camera)
		return getResponseWithDigest(
			snapshotUri,
			HttpMethod.GET,
			UsernamePasswordCredentials(camera.username, camera.password)
		)
	}
	
	/**
	 * 获取原始的视频链接，并添加身份认证信息
	 */
	fun getVideoUri(@NonNull camera: Camera): String {
		var uri: String? = null
		val lock = MyLock()
		onvifManager.getMediaStreamURI(
			camera.toDevice(),
			onvifControlService.getOnvifMediaProfiles(camera)[0],
		) { _, _, streamUri ->
			uri = streamUri
			lock.aSignal()
		}
		lock.aWait(8, TimeUnit.SECONDS)
		if (uri == null) {
			throw OnvifResponseTimeoutException()
		}
		//视频链接附上认证信息
		var i = uri!!.indexOf("://")
		if (i == -1) {
			throw IllegalArgumentException()
		}
		i += 3
		uri = uri!!.substring(0, i) + URLEncoder.encode(
			camera.username,
			StandardCharsets.UTF_8
		) + ":" + URLEncoder.encode(camera.password, StandardCharsets.UTF_8) + "@" + uri!!.substring(i)
		return uri!!
	}
	
	/**
	 * 获取预置位列表
	 *
	 * @param camera 摄像头详情
	 */
	fun getPresetList(@NonNull camera: Camera): List<Preset> {
		var presetList: List<Preset>? = null
		val lock = MyLock()
		onvifManager.getPresets(
			camera.toDevice(),
			onvifControlService.getOnvifMediaProfiles(camera)[0]
		) { _, _, presets ->
			presetList = presets.map { it.toPreset(onvifControlService.getOnvifInfo(camera).manufacturer) }
			lock.aSignal()
		}
		lock.aWait(8, TimeUnit.SECONDS)
		
		if (presetList == null) {
			throw OnvifResponseTimeoutException()
		}
		
		return presetList!!
	}
	
	/**
	 * Onvif设备绝对移动（转动到指定的坐标）
	 *
	 * @param x 转换后的p，范围0~1
	 * @param y 转换后的t，范围0~1
	 * @param z 转换后的zoom，范围0~1
	 */
	fun ptzAbsoluteMove(@NonNull camera: Camera, x: Double, y: Double, z: Double) {
		onvifManager.absoluteMove(camera.toDevice(), onvifControlService.getOnvifMediaProfiles(camera)[0], x, y, z)
	}
	
	/**
	 * 获取设备方位角信息（不含视场角）
	 */
	fun getPtzInfo(@NonNull camera: Camera): PtzInfo {
		var onvifStatus: OnvifStatus? = null
		val lock = MyLock()
		onvifManager.getStatus(
			camera.toDevice(), onvifControlService.getOnvifMediaProfiles(camera)[0]
		) { _, _, onvifStatus1 ->
			onvifStatus = onvifStatus1
			lock.aSignal()
		}
		lock.aWait(8, TimeUnit.SECONDS)
		if (onvifStatus == null) {
			throw OnvifResponseTimeoutException()
		}
		return onvifStatus!!.toPtzInfo()
	}
	
}
