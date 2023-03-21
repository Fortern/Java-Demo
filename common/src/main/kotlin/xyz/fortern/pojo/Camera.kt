package xyz.fortern.pojo

import java.io.Serializable

data class Camera(
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
	
	/**
	 * 纬度，范围为[-90,90]。东经为正数，西经为负数。
	 */
	var latitude: Double? = null
	
	/**
	 * 经度，范围为(-180,180]。北纬为正数，南纬为负数。
	 */
	var longitude: Double? = null
	
	/**
	 * 所属区域ID
	 */
	var regionId: Long? = null
	
}
