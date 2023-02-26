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
	
	@PostMapping("/getInfo")
	fun cameraInfo(id: Int): ResponseEntity<String> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found.")
		cameraService.getInfo(camera)
		return ResponseEntity.ok(null)
	}
	
	@PostMapping("/profile")
	fun cameraProfile(id: Int): ResponseEntity<*> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found")
		val mediaProfiles = cameraService.getMediaProfiles(camera)
		return ResponseEntity.ok(mediaProfiles)
	}
	
	@PostMapping("/ptz")
	fun cameraPtzControl(id: Int, p: Double, t: Double, z: Double): ResponseEntity<String> {
		val camera = cameraService.getCameraById(id) ?: return ResponseEntity.ok("camera not found")
		cameraService.ptzAbsoluteMove(camera, p, t, z)
		return ResponseEntity.ok(null)
	}
}