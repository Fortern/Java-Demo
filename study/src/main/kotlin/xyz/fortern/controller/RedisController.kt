package xyz.fortern.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import xyz.fortern.pojo.camera.Camera

@Controller
@RequestMapping("/redis")
class RedisController(
	redisTemplate: RedisTemplate<String, Any>
) {
	private final val logger: Logger = LoggerFactory.getLogger(this.javaClass)
	
	private val opsForValue:ValueOperations<String, Any>
	
	init {
		this.opsForValue = redisTemplate.opsForValue()
	}
	
	@PostMapping("/set-key-value")
	fun setKeyValue(key: String, value: String): ResponseEntity<*> {
		opsForValue.set(key, value)
		return ResponseEntity.ok("ok")
	}
	
	@PostMapping("/set-key")
	fun setKeyValue(key: String): ResponseEntity<*> {
		val camera = Camera(2, "1.1.1.1", 40, "user", "pass")
		opsForValue.set(key, camera)
		return ResponseEntity.ok("ok")
	}
	
	@PostMapping("/get-key-value")
	fun getKeyValue(key: String): ResponseEntity<*> {
		val value = opsForValue.get(key) as String
		
		return ResponseEntity.ok(value)
	}
	
	@PostMapping("/get-key")
	fun getKey(key: String): ResponseEntity<*> {
		val value = opsForValue.get(key) as Camera
		return ResponseEntity.ok(value)
	}
	
}