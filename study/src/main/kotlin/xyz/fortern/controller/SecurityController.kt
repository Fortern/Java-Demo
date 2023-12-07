package xyz.fortern.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/security")
open class SecurityController {
	//PreAuthorize注解基于动态代理，因此被代理的方法不能为final
	@PreAuthorize("hasAuthority('common')")
	@RequestMapping("/common")
	open fun common(): ResponseEntity<String> {
		return ResponseEntity.ok("common")
	}
	
	@RequestMapping("/index")
	open fun index(): ResponseEntity<String> {
		return ResponseEntity.ok("index")
	}
	
	@PreAuthorize("hasAuthority('normal')")
	@RequestMapping("/normal")
	open fun normal(): ResponseEntity<String> {
		return ResponseEntity.ok("normal")
	}
	
	@PostMapping("/login")
	open fun login(): ResponseEntity<String> {
		val authenticationToken = UsernamePasswordAuthenticationToken("user", null, emptyList())
		SecurityContextHolder.getContext().authentication = authenticationToken
		return ResponseEntity.ok("")
	}
}
