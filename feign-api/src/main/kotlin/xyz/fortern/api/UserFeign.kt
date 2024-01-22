package xyz.fortern.api

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import xyz.fortern.pojo.user.User

@FeignClient(name = "user")
interface UserFeign {
	@PostMapping("/user/{id}")
	fun getUserById(@PathVariable id: Int): User
}