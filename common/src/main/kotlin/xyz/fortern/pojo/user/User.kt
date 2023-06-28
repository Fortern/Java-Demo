package xyz.fortern.pojo.user

/**
 * 用户信息实体类
 */
data class User(
		/**
		 * id
		 */
		var id: Int,
		/**
		 * 用户名
		 */
		var username: String,
		/**
		 * 加密的密码
		 */
		var password: String,
		/**
		 * 电话
		 */
		var phone: String?,
		/**
		 * 权限列表
		 */
		var permissions: List<String>,
		/**
		 * 区域
		 */
		var region: Long?,
		/**
		 * 角色ID
		 */
		var roleId: Int
)
