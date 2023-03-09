package xyz.fortern.pojo

import java.io.Serializable

data class OnvifCamera(
	/**
	 * id
	 */
	var id: Int?,
	/**
	 * ip地址
	 */
	var ip: String?,
	/**
	 * 端口号
	 */
	var port: Int?,
	/**
	 * 登录用户名
	 */
	var username: String?,
	/**
	 * 登录密码
	 */
	var password: String?,
	
	) : Serializable {
	
	/**
	 * 预置位列表
	 */
	var presetList: List<Preset>? = null
}
