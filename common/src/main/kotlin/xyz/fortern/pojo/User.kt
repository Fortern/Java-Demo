package xyz.fortern.pojo

/**
 * 用户信息实体类
 */
data class User(
	var id: Int,
	var username: String,
	var password: String,
	var phone: String?,
	var permissions: List<String>,
)
