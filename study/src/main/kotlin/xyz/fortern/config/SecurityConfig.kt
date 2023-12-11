package xyz.fortern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {
	@Bean
	open fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http.csrf().disable()
		//SessionCreationPolicy默认为IF_REQUIRED
		//http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
		http.authorizeRequests().antMatchers("/test/**").permitAll()
			.antMatchers("/file/**").permitAll()
			.antMatchers("/**.html").permitAll()
			.antMatchers("/security/login").permitAll()
			.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
			.anyRequest().authenticated()
		http.formLogin()
		//禁用requestCache，避免生成记录上次请求的Session，或者使用requestCache(new NullRequestCache())
		http.requestCache()/*.requestCache(new NullRequestCache())*/.disable()
		return http.build()
	}
}
