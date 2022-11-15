package xyz.fortern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class SecurityConfig {
	@Bean
	open fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http.csrf().disable()
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
		http.authorizeRequests().antMatchers("/test/**").permitAll()
			.antMatchers("/file/**").permitAll()
			.antMatchers("/**.html").permitAll()
			.anyRequest().authenticated()
		//禁用requestCache，避免生成记录上次请求的Session，或者使用requestCache(new NullRequestCache())
		http.requestCache()/*.requestCache(new NullRequestCache())*/.disable()
		return http.build()
	}
}
