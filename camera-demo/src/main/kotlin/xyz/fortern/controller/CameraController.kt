package xyz.fortern.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import xyz.fortern.pojo.OnvifCamera
import xyz.fortern.service.CameraService

@Controller
@RequestMapping("/camera")
class CameraController(
	private val cameraService: CameraService,
) {
	/**
	 * 添加新的摄像头
	 */
	@PostMapping("/create")
	fun addCamera(ip: String, port: Int, username: String, password: String): ResponseEntity<String> {
		val onvifDevice = OnvifCamera(null, ip, port, username, password)
		cameraService.addNewCamera(onvifDevice)
		return ResponseEntity.ok("ok")
	}
	
	/**
	 * 删除一个摄像头
	 */
	@PostMapping("/delete/{id}")
	fun deleteCamera(@PathVariable id: Int): ResponseEntity<String> {
		cameraService.deleteById(id)
		return ResponseEntity.ok("ok")
	}
	
	/**
	 * 更新一个摄像头
	 */
	@PostMapping("/update")
	fun updateCamera(camera: OnvifCamera): ResponseEntity<String> {
		cameraService.updateCamera(camera)
		return ResponseEntity.ok("ok")
	}
	
	/**
	 * 获取摄像头详情
	 */
	@PostMapping("/get/{id}")
	fun getCamera(@PathVariable id: Int): ResponseEntity<*> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found.")
		return ResponseEntity.ok(camera)
	}
	
	@PostMapping("/getInfo/{id}")
	fun cameraInfo(@PathVariable id: Int): ResponseEntity<*> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found.")
		val info = cameraService.getOnvifInfo(camera)
		return ResponseEntity.ok(info)
	}
	
	@PostMapping("/profile/{id}")
	fun cameraProfile(@PathVariable id: Int): ResponseEntity<*> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found")
		val mediaProfiles = cameraService.getOnvifMediaProfiles(camera)
		return ResponseEntity.ok(mediaProfiles)
	}
	
	@PostMapping("/ptz/{id}")
	fun cameraPtzControl(@PathVariable id: Int, p: Double, t: Double, z: Double): ResponseEntity<String> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found")
		cameraService.ptzAbsoluteMove(camera, p, t, z)
		return ResponseEntity.ok(null)
	}
}