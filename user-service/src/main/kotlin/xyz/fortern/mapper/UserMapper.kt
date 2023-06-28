package xyz.fortern.mapper

import xyz.fortern.pojo.user.User
import xyz.fortern.pojo.user.UserSecurity

/**
 * User数据库操作实体类
 */
interface UserMapper {
	/**
	 * 插入一个新用户
	 */
	fun insertUser(user: User): Int
	
	/**
	 * 根据Username查询用户
	 */
	fun selectUserByUsername(username: String): User
	
	/**
	 * 根据ID查询UserSecurity信息
	 */
	fun selectUserSecurityById(id: Int): UserSecurity
	
}
