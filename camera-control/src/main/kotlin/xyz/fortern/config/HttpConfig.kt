package xyz.fortern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.io.IOException

@Configuration
class HttpConfig {
	
	@Bean
	fun restTemplate(): RestTemplate {
		val restTemplate = RestTemplate()
		restTemplate.errorHandler = object : ResponseErrorHandler {
			@Throws(IOException::class)
			override fun hasError(response: ClientHttpResponse): Boolean {
				return false
			}
			
			@Throws(IOException::class)
			override fun handleError(response: ClientHttpResponse) {
			}
		}
		return restTemplate
	}
}