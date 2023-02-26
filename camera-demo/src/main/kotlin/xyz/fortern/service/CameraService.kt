package xyz.fortern.service

import be.teletask.onvif.OnvifManager
import be.teletask.onvif.listeners.OnvifResponseListener
import be.teletask.onvif.models.OnvifDevice
import be.teletask.onvif.models.OnvifDeviceInformation
import be.teletask.onvif.models.OnvifMediaProfile
import be.teletask.onvif.responses.OnvifResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch
import xyz.fortern.exception.OnvifResponseTimeoutException
import xyz.fortern.mapper.CameraMapper
import xyz.fortern.pojo.OnvifCamera
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Service
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
	@Cacheable(value = ["camera"], key = "#id")
	fun getCameraById(id: Int): OnvifCamera? {
		return cameraMapper.getById(id)
	}
	
	/**
	 * 根据主键删除一条摄像头信息
	 *
	 * @param id 摄像头的ID
	 */
	@CacheEvict(value = ["camera"], key = "#id")
	fun deleteById(id: Int) {
		cameraMapper.deleteById(id)
	}
	
	/**
	 * 根据主键更新一条摄像头信息
	 *
	 * @param camera 摄像头详情
	 */
	@CacheEvict(value = ["camera"], key = "#camera.id")
	fun updateCamera(camera: OnvifCamera) {
		cameraMapper.updateById(camera)
	}
	
	/**
	 * 添加一个新摄像头信息
	 */
	fun addNewCamera(camera: OnvifCamera) {
		cameraMapper.insert(camera)
	}
	
	/**
	 * 获取设备信息
	 *
	 * @param device Onvif设备
	 */
	fun getInfo(camera: OnvifCamera): OnvifDeviceInformation {
		var info: OnvifDeviceInformation? = null
		val stopWatch = StopWatch("测试")
		stopWatch.start("测试1")
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
		
		logger.info("onvifDeviceInformation: {}", info)
		stopWatch.stop()
		logger.info("方法执行时间：{}ms", stopWatch.lastTaskTimeMillis)
		return info as OnvifDeviceInformation
	}
	
	/**
	 * 获取MediaProfiles
	 *
	 * @param camera Onvif设备
	 * @throws OnvifResponseTimeoutException 请求Onvif设备信息超时
	 */
	@NonNull
	@Cacheable(value = ["profiles"], key = "#camera.id")
	fun getMediaProfiles(camera: OnvifCamera): List<OnvifMediaProfile> {
		var mediaProfiles: List<OnvifMediaProfile>? = null
		val stopWatch = StopWatch("测试")
		stopWatch.start("测试1")
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
		logger.info("mediaProfiles: {}", mediaProfiles)
		stopWatch.stop()
		logger.info("方法执行时间：{}ms", stopWatch.lastTaskTimeMillis)
		return mediaProfiles as List<OnvifMediaProfile>
	}
	
	fun ptzAbsoluteMove(camera: OnvifCamera, p: Double, t: Double, z: Double) {
		val mediaProfiles = getMediaProfiles(camera)
		
		onvifManager.absoluteMove(
			OnvifDevice(camera.ip + ":" + camera.port, camera.username, camera.password),
			mediaProfiles[0],
			p,
			t,
			z
		)
		
	}
}
