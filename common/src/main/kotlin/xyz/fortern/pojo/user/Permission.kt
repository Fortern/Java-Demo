package xyz.fortern.pojo.user

/**
 * 权限实体类
 */
enum class Permission(
		/**
		 * 权限的描述
		 */
		val key: String,
		/**
		 * 权限的二进制表示
		 */
		val value: Long,
) {
	/**
	 * 控制摄像头的权限
	 */
	PTZ_CONTROL("控制摄像头", 1L),
	
	/**
	 * 编辑系统配置的权限
	 */
	SYSTEM_CONF("编辑系统配置", 2L),
	;
}

/**
 * 一个权限集合是否包含某一权限
 */
fun hasPermission(userPermission: Long, permission: Permission): Boolean {
	return userPermission and (permission.value) == permission.value
}
