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
 *
 * @param userPermission 包含全部权限信息的ID
 * @param permission     要检查是否存在的权限
 *
 * @return 拥有该权限返回true，否则返回false
 */
fun hasPermission(userPermission: Long, permission: Permission): Boolean {
	return userPermission and (permission.value) == permission.value
}

/**
 * 修改一个权限ID，使其具有给定的权限
 */
fun givePermission(userPermission: Long, permission: Permission): Long {
	return userPermission or permission.value
}
