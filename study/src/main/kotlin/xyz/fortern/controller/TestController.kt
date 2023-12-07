package xyz.fortern.controller

import com.alibaba.fastjson.JSON
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.fortern.pojo.user.User
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class TestController(
	private val redisTemplate: RedisTemplate<String, Any>,
) {
	private val valueOperations = redisTemplate.opsForValue()
	private val hashOperations = redisTemplate.opsForHash<String, Any>()
	
	private val logger = LoggerFactory.getLogger(this.javaClass)
	
	/**
	 * 继承一个类，并创建单例
	 */
	private val cache: Map<String, Any?> = object : LinkedHashMap<String, Any?>(6, 1.0f, true) {
		//元素超过5个，就删除最早的一个
		override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Any?>) = size > 5
	}
	
	@GetMapping("/str")
	fun sessionTest(request: HttpServletRequest): ResponseEntity<String> {
		/*
		 * 在使用SpringSecurity时，哪些情况会生成Session？
		 *
		 * - 仅启用SpringSession，不会自动创建Session
		 * - 开启SpringSecurity(无配置),401,会自动创建Session
		 * - 开启SpringSecurity(SecurityFilterChain),200,不会自动创建Session
		 * - 继续配置authorizeRequests，放行的接口200不生成Session，拦截的接口403生成Session
		 * - 将sessionCreationPolicy设为NEVER，依旧会生成Session
		 * - 禁用requestCache后，访问需要身份认证的接口，不再产生Session
		 */
		val create = request.getParameter("create").toBoolean()
		val session = request.getSession(create)
		println("session:${session?.id}")
		return ResponseEntity.ok(null)
	}
	
	@GetMapping("/common")
	fun login(request: HttpServletRequest?): ResponseEntity<String>? {
		return ResponseEntity.ok("ok")
	}
	
	@PostMapping("/redis/set/{op}")
	fun redisSet(
		@PathVariable op: String,
		key: String,
		value: String,
		@RequestParam(required = false) field: String?,
	): ResponseEntity<String>? {
		if (op == "hash")
			field?.let { hashOperations.put(key, it, value) }
		else
			valueOperations.set(key, value)
		return ResponseEntity.ok("ok")
	}
	
	@GetMapping("/redis/get/{op}")
	fun redisGet(
		@PathVariable op: String,
		key: String,
		@RequestParam(required = false) field: String?,
	): ResponseEntity<String>? {
		val result: String = (if (op == "hash")
			if (field == null)
				JSON.toJSONString(hashOperations.entries(key))
			else
				(hashOperations.get(key, field) as String)
		else
			valueOperations.get(key)) as String
		return ResponseEntity.ok(result)
	}
	
	@GetMapping("/redis/delete")
	fun redisDelete(key: String): ResponseEntity<String> {
		val delete = redisTemplate.delete(key)
		return if (delete == true) {
			ResponseEntity.ok("ok")
		} else {
			ResponseEntity.ok("no")
		}
	}
	
	/**
	 * 读取多个参数，SpringBoot自动封装为对象。逗号视为数组元素分隔符。
	 */
	@PostMapping("/readuser")
	fun readUser(user: User): ResponseEntity<User> {
		return ResponseEntity.ok(user)
	}
	
	/**
	 * 测试SessionId
	 */
	@Operation(summary = "sessionId测试", description = "在日志中输出SessionId")
	@PostMapping("/session")
	fun sessionTest2(request: HttpServletRequest): ResponseEntity<String> {
		val session = request.session
		logger.info("sessionId: ${session.id}")
		return ResponseEntity.ok("")
	}
	
}
