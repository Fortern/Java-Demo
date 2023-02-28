package xyz.fortern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * SpringSecurity安全配置
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http.csrf().disable()
		http.authorizeRequests()
			.antMatchers("/login").permitAll()
			.anyRequest().authenticated()
		http.formLogin()
		http.requestCache().disable()
		return http.build()
	}
}