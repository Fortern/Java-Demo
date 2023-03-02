package xyz.fortern.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import xyz.fortern.constant.CAMERA_CACHE
import xyz.fortern.mapper.CameraMapper
import xyz.fortern.pojo.OnvifCamera

/**
 * 摄像头的增删改查操作
 */
@Service
@CacheConfig(cacheNames = [CAMERA_CACHE])
class CameraService(
	val cameraMapper: CameraMapper,
) {
	private final val logger: Logger = LoggerFactory.getLogger(this.javaClass)
	
	/**
	 * 获取摄像头详情，从缓存中获取或从数据库获取
	 *
	 * @param id 摄像头的ID
	 */
	@Cacheable(key = "#id", unless = "#result == null")
	fun getCameraById(id: Int): OnvifCamera? {
		return cameraMapper.getById(id)
	}
	
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
	 *
	 * @param camera 摄像头详情
	 */
	@CachePut(key = "#camera.id")
	fun addNewCamera(camera: OnvifCamera) {
		cameraMapper.insert(camera)
	}
	
}