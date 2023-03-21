package xyz.fortern.api

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import xyz.fortern.pojo.Camera

/**
 * 通用的Camera模块API接口
 */
@FeignClient(name = "camera")
interface CameraFeign {
	
	/**
	 * 根据ID获取摄像头
	 */
	@PostMapping("/get/{id}")
	fun getById(@PathVariable id: Int): Camera?
	
}
