package xyz.fortern.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import xyz.fortern.pojo.user.User
import java.util.*

@Component
class CustomAuthProvider : AuthenticationProvider {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)
	
	private val userMap = mapOf(
		Pair(0, User(0, "Fortern", "123456", null, Collections.emptyList(), 0, null, 1)),
		Pair(1, User(1, "Maxin", "123456", null, Collections.emptyList(), 0, null, 1))
	)
	
	override fun authenticate(authentication: Authentication): Authentication? {
		val username = (authentication.principal as String).toInt()
		val password = authentication.credentials.toString()
		val user = userMap[username]
		user?.let {
			if (user.password == password) {
				val list = user.permissions.map { SimpleGrantedAuthority(it) }
				logger.info("登录成功")
				return UsernamePasswordAuthenticationToken(user, null, list)
			}
		}
		return null
	}
	
	override fun supports(authentication: Class<*>?): Boolean {
		return true
	}
}
