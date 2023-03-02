package xyz.fortern.config

import com.alibaba.fastjson2.support.config.FastJsonConfig
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
	
	/**
	 * 配置FastJson2转换器
	 */
	override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
		val converter = FastJsonHttpMessageConverter()
		val config = FastJsonConfig()
		// yyyy-MM-dd'T'HH:mm:ss.SSSZ
		config.dateFormat = "millis"
		converter.fastJsonConfig = config
		converter.supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)
		converters.add(0, converter)
	}
}
