package xyz.fortern.service

import be.teletask.onvif.OnvifManager
import be.teletask.onvif.listeners.OnvifResponseListener
import be.teletask.onvif.models.OnvifDevice
import be.teletask.onvif.models.OnvifDeviceInformation
import be.teletask.onvif.models.OnvifMediaProfile
import be.teletask.onvif.responses.OnvifResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import xyz.fortern.constant.CAMERA_CACHE
import xyz.fortern.constant.ONVIF_INFO_CACHE
import xyz.fortern.constant.ONVIF_PROFILE_CACHE
import xyz.fortern.exception.OnvifResponseTimeoutException
import xyz.fortern.mapper.CameraMapper
import xyz.fortern.pojo.OnvifCamera
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Service
@CacheConfig(cacheNames = [CAMERA_CACHE])
class CameraService(
	private val cameraMapper: CameraMapper,
) {
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
	 * 获取摄像头详情，从缓存中获取或从数据库获取
	 *
	 * @param id 摄像头的ID
	 */
	@Cacheable(key = "#id")
	fun getCameraById(id: Int): OnvifCamera? = cameraMapper.getById(id)
	
	/**
	 * 根据主键删除一条摄像头信息
	 *
	 * @param id 摄像头的ID
	 */
	@CacheEvict(key = "#id")
	fun deleteById(id: Int) {
		cameraMapper.deleteById(id)
	}
	
	/**
	 * 根据主键更新一条摄像头信息
	 *
	 * @param camera 摄像头详情
	 */
	@CachePut(key = "#camera.id")
	fun updateCamera(camera: OnvifCamera) {
		cameraMapper.updateById(camera)
	}
	
	/**
	 * 添加一个新摄像头信息
	 */
	@CachePut(key = "#camera.id")
	fun addNewCamera(camera: OnvifCamera) {
		cameraMapper.insert(camera)
	}
	
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
			mLock.lock()
			condition.signal()
			mLock.unlock()
		}
		mLock.lock()
		condition.await(8, TimeUnit.SECONDS)
		mLock.unlock()
		
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
			mLock.lock()
			condition.signal()
			mLock.unlock()
		}
		mLock.lock()
		condition.await(8, TimeUnit.SECONDS)
		mLock.unlock()
		
		if (mediaProfiles === null) {
			throw OnvifResponseTimeoutException()
		}
		return mediaProfiles as List<OnvifMediaProfile>
	}
	
	fun ptzAbsoluteMove(camera: OnvifCamera, p: Double, t: Double, z: Double) {
		val mediaProfiles = getOnvifMediaProfiles(camera)
		
		onvifManager.absoluteMove(
			OnvifDevice(camera.ip + ":" + camera.port, camera.username, camera.password),
			mediaProfiles[0],
			p,
			t,
			z
		)
		
	}
}
