package xyz.fortern.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import xyz.fortern.service.RegionService

@Controller
@RequestMapping("/region")
class RegionController(
	val regionService: RegionService
) {
	@PostMapping("/get/{id}")
	fun getRegion(@PathVariable id: Int): ResponseEntity<Any> {
		val region = regionService.getById(id)
		return ResponseEntity.ok(region)
	}
}