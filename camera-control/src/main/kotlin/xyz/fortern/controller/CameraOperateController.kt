package xyz.fortern.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import xyz.fortern.api.CameraFeign
import xyz.fortern.service.CameraControlService

@Controller
class CameraOperateController(
	private val cameraControlService: CameraControlService,
	private val cameraFeign: CameraFeign,
) {
	@PostMapping("/getInfo/{id}")
	fun cameraInfo(@PathVariable id: Int): ResponseEntity<*> {
		val camera = cameraFeign.getById(id) ?: return ResponseEntity.ok("camera not found.")
		val info = cameraControlService.getOnvifInfo(camera)
		return ResponseEntity.ok(info)
	}
	
	@PostMapping("/profile/{id}")
	fun cameraProfile(@PathVariable id: Int): ResponseEntity<*> {
		val camera = cameraFeign.getById(id) ?: return ResponseEntity.ok("camera not found")
		val mediaProfiles = cameraControlService.getOnvifMediaProfiles(camera)
		return ResponseEntity.ok(mediaProfiles)
	}
	
	@PostMapping("/ptz/{id}")
	fun cameraPtzControl(@PathVariable id: Int, p: Double, t: Double, z: Double): ResponseEntity<String> {
		val camera = cameraFeign.getById(id) ?: return ResponseEntity.ok("camera not found")
		cameraControlService.ptzAbsoluteMove(camera, p, t, z)
		return ResponseEntity.ok(null)
	}
}
