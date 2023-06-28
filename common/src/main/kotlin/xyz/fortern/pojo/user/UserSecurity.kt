package xyz.fortern.pojo.user

import java.time.Instant

class UserSecurity(
		/**
		 * 用户ID
		 */
		val id: Int,
		/**
		 * 加密的密码
		 */
		var password: String,
) {
	/**
	 * 到期时间
	 */
	var expiration: Instant? = null
	
	/**
	 * 上次登录时间
	 */
	var lastLoginDate: Instant? = null
	
	/**
	 * 用户创建时间
	 */
	var createDate: Instant? = null
}
