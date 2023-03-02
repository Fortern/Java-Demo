package xyz.fortern.pojo

import java.util.*

/**
 * 模仿SpringBoot的默认错误返回。
 *
 * 仅当响应错误信息时使用此类。
 *
 * @see org.springframework.boot.web.servlet.error.DefaultErrorAttributes
 */

data class SpringResponse(
	/**
	 * 应当与http响应状态码一致
	 */
	val status: Int,
) {
	/**
	 * 响应时间。无需手动设置。
	 */
	var timestamp: Date? = null
	
	/**
	 * 错误解释。一般无需手动设置。
	 *
	 * 如果没有手动设置，则会被自动设置为httpStatus的reasonPhrase
	 */
	var error: String? = null
}
