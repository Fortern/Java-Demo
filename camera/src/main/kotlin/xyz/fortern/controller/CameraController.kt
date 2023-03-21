package xyz.fortern.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import xyz.fortern.pojo.Camera
import xyz.fortern.pojo.SpringResponse
import xyz.fortern.service.CameraService

@RestController
class CameraController(
	val cameraService: CameraService,
) {
	/**
	 * 添加新的摄像头
	 */
	@PostMapping("/create")
	fun addCamera(ip: String, port: Int, username: String, password: String): Any? {
		val onvifDevice = Camera(null, ip, port, username, password)
		cameraService.addNewCamera(onvifDevice)
		return null
	}
	
	/**
	 * 删除一个摄像头
	 */
	@PostMapping("/delete/{id}")
	fun deleteCamera(@PathVariable id: Int): Any? {
		cameraService.deleteById(id)
		return null
	}
	
	/**
	 * 更新一个摄像头
	 */
	@PostMapping("/update")
	fun updateCamera(camera: Camera): Any? {
		cameraService.updateCamera(camera)
		return null
	}
	
	/**
	 * 更新一个摄像机的坐标信息
	 */
	@PostMapping("/set-coordinate/{id}")
	fun updateCameraCoordinate(@PathVariable id: Int, latitude: Double, longitude: Double): Any {
		return cameraService.updateCoordinateById(id, latitude, longitude)
			?: SpringResponse(HttpStatus.NOT_FOUND.value())
	}
	
	/**
	 * 获取摄像头详情
	 */
	@PostMapping("/get/{id}")
	fun getCamera(@PathVariable id: Int): Any {
		return cameraService.getCameraById(id) ?: SpringResponse(HttpStatus.NOT_FOUND.value())
	}
}
