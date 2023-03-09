package xyz.fortern.pojo

/**
 * 方位角与视场角信息。
 *
 * 查询摄像头方位角时，用该类封装。
 */
data class PtzInfo(
	/**
	 * pan
	 */
	var pan: Float?,
	/**
	 * title
	 */
	var tilt: Float?,
	/**
	 * zoom
	 */
	var zoom: Float?,
) {
	/**
	 * 水平视场角
	 */
	var horizontal: Float? = null
	
	/**
	 * 竖直视场角
	 */
	var vertical: Float? = null
}
