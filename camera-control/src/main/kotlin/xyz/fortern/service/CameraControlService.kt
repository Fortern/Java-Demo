package xyz.fortern.service

import be.teletask.onvif.OnvifManager
import be.teletask.onvif.listeners.OnvifResponseListener
import be.teletask.onvif.models.OnvifDevice
import be.teletask.onvif.models.OnvifDeviceInformation
import be.teletask.onvif.models.OnvifMediaProfile
import be.teletask.onvif.responses.OnvifResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import xyz.fortern.constant.ONVIF_INFO_CACHE
import xyz.fortern.constant.ONVIF_PROFILE_CACHE
import xyz.fortern.exception.OnvifResponseTimeoutException
import xyz.fortern.pojo.OnvifCamera
import xyz.fortern.util.aSignal
import xyz.fortern.util.aWait
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 对Onvif摄像头进行控制
 */
@Service
class CameraControlService {
	private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
	
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
	fun getOnvifInfo(camera: OnvifCamera): OnvifDeviceInformation {
		var info: OnvifDeviceInformation? = null
		val mLock: Lock = ReentrantLock()
		val condition = mLock.newCondition()
		onvifManager.getDeviceInformation(
			OnvifDevice(
				camera.ip + ":" + camera.port,
				camera.username,
				camera.password
			)
		) { _, deviceInformation ->
			info = deviceInformation
			mLock.aSignal(condition)
		}
		mLock.aWait(condition, 8, TimeUnit.SECONDS)
		
		if (info === null) {
			throw OnvifResponseTimeoutException()
		}
		return info as OnvifDeviceInformation
	}
	
	/**
	 * 获取MediaProfiles
	 *
	 * @param camera Onvif设备
	 * @throws OnvifResponseTimeoutException 请求Onvif设备信息超时
	 */
	@NonNull
	@Cacheable(value = [ONVIF_PROFILE_CACHE], key = "#camera.id")
	fun getOnvifMediaProfiles(camera: OnvifCamera): List<OnvifMediaProfile> {
		var mediaProfiles: List<OnvifMediaProfile>? = null
		val mLock: Lock = ReentrantLock()
		val condition = mLock.newCondition()
		onvifManager.getMediaProfiles(
			OnvifDevice(
				camera.ip + ":" + camera.port,
				camera.username,
				camera.password
			)
		) { _, mediaProfilesReceived ->
			mediaProfiles = mediaProfilesReceived
			mLock.aSignal(condition)
		}
		mLock.aWait(condition, 8, TimeUnit.SECONDS)
		
		if (mediaProfiles === null) {
			throw OnvifResponseTimeoutException()
		}
		return mediaProfiles as List<OnvifMediaProfile>
	}
	
	/**
	 * Onvif设备绝对移动（转动到指定的坐标）
	 *
	 * @param x 转换后的p，范围0~1
	 * @param y 转换后的t，范围0~1
	 * @param z 转换后的zoom，范围0~1
	 */
	fun ptzAbsoluteMove(camera: OnvifCamera, x: Double, y: Double, z: Double) {
		val mediaProfiles = getOnvifMediaProfiles(camera)
		
		onvifManager.absoluteMove(
			OnvifDevice(camera.ip + ":" + camera.port, camera.username, camera.password),
			mediaProfiles[0],
			x,
			y,
			z
		)
		
	}
}
