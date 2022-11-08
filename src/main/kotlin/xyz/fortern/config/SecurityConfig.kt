package xyz.fortern.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		http.authorizeRequests().antMatchers("/test/common").permitAll().anyRequest().authenticated();
		//禁用requestCache，避免生成记录上次请求的Session，或者使用requestCache(new NullRequestCache())
		http.requestCache()/*.requestCache(new NullRequestCache())*/.disable();
		return http.build();
	}
}
