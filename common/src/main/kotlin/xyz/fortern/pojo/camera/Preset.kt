package xyz.fortern.pojo.camera

/**
 * 预置位
 */
data class Preset(
	/**
	 * 名称
	 */
	var name: String,
	/**
	 * 平台ID
	 */
	var token: String,
	/**
	 * pan
	 */
	var pan: Float,
	/**
	 * tilt
	 */
	var titl: Float,
	/**
	 * zoom
	 */
	var zoom: Float,
)
