package xyz.fortern.controller

import be.teletask.onvif.models.OnvifDevice
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import xyz.fortern.pojo.OnvifCamera
import xyz.fortern.service.CameraControlService

@Controller
@RequestMapping("/camera")
class CameraController(
	private val cameraControlService: CameraControlService,
) {
	/**
	 * 添加新的摄像头
	 */
	@PostMapping("/create")
	fun addCamera(ip: String, port: Int, username: String, password: String): ResponseEntity<*> {
		val onvifDevice = OnvifCamera(null, ip, port, username, password)
		cameraControlService.addNewCamera(onvifDevice)
		return ResponseEntity.ok(null)
	}
	
	@PostMapping("/getInfo")
	fun cameraInfo(ip: String, port: String, username: String, password: String): ResponseEntity<*> {
		val onvifDevice = OnvifDevice("$ip:$port", username, password)
		cameraControlService.getInfo(onvifDevice)
		return ResponseEntity.ok(null)
	}
	
	@PostMapping("/profile")
	fun cameraProfile(id: Int): ResponseEntity<String> {
		val camera = cameraControlService.getCameraById(id)
		if (camera === null) {
			return ResponseEntity.ok("camera not found")
		}
		val mediaProfiles = cameraControlService.getMediaProfiles(camera)
		return ResponseEntity.ok(mediaProfiles.toString())
	}
	
	@PostMapping("/ptz")
	fun cameraPtzControl(id: Int, p:Double, t:Double, z:Double): ResponseEntity<*> {
		val camera = cameraControlService.getCameraById(id)
		if (camera === null) {
			return ResponseEntity.ok("camera not found")
		}
		cameraControlService.ptzAbsoluteMove(camera, p, t, z)
		return ResponseEntity.ok(null)
	}
}