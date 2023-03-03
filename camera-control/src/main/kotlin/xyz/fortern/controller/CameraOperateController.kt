package xyz.fortern.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import xyz.fortern.api.CameraFeign
import xyz.fortern.pojo.SpringResponse
import xyz.fortern.service.CameraControlService

@RestController
class CameraOperateController(
	private val cameraControlService: CameraControlService,
	private val cameraFeign: CameraFeign,
) {
	@PostMapping("/getInfo/{id}")
	fun cameraInfo(@PathVariable id: Int): Any? {
		val camera = cameraFeign.getById(id) ?: return SpringResponse(HttpStatus.NOT_FOUND.value())
		return cameraControlService.getOnvifInfo(camera)
	}
	
	@PostMapping("/profile/{id}")
	fun cameraProfile(@PathVariable id: Int): Any? {
		val camera = cameraFeign.getById(id) ?: return SpringResponse(HttpStatus.NOT_FOUND.value())
		return cameraControlService.getOnvifMediaProfiles(camera)
	}
	
	@ResponseBody
	@PostMapping("/ptz/{id}")
	fun cameraPtzControl(@PathVariable id: Int, p: Double, t: Double, z: Double): Any? {
		val camera = cameraFeign.getById(id) ?: return SpringResponse(HttpStatus.NOT_FOUND.value())
		cameraControlService.ptzAbsoluteMove(camera, p, t, z)
		return null
	}
}
