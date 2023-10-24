package xyz.fortern.pojo.user

import java.lang.Long.numberOfLeadingZeros
import java.lang.Long.numberOfTrailingZeros

/**
 * 区域信息。摄像机需要指定所在的区域。
 */
data class Region(
	/**
	 * 区域编码，包含个区域层级与编码信息信息。
	 *
	 * 按照二进制的方式进行解析，从低位到高位每12位分成一段，就像IP地址一样，形如
	 * 0000.000000000000.000000000000.000000000000.000000000000.000000000000
	 *
	 * 最左四位弃用。右侧的五段表示五级区域。例如，1.0.0.0.0表示海南省，
	 * 1.1.0.0.0表示海口市，1.1.1.0.0表示琼山区，1.1.1.1.0表示云龙镇，
	 * 1.1.1.1.1表示云蛟村，1.2.0.0.0表示三亚市，1.2.1.0.0表示海棠区。
	 * 无法表示深度超过5的区域。
	 */
	val id: Long,
	/**
	 * 区域的名称
	 */
	var name: String,
) {
	/**
	 * 区域的第三方编码
	 */
	var code: String? = null
	
	/**
	 * 区域内部的排序
	 */
	var sort: Int = 0
	
	/**
	 * 该组织节点的等级，越小表示范围越大。
	 *
	 * @return 该组织节点的等级，范围1~5
	 */
	fun level(): Int = level(this.id)
	
	companion object {
		/**
		 * 判断区域a是否为区域b的子区域
		 *
		 * @param i 区域a的id
		 * @param o 区域b的id
		 * @return true如果i是o的子区域，false如果i不是o的子区域
		 */
		fun inside(i: Long, o: Long): Boolean {
			val a = i shl 4
			val b = o shl 4
			//c 为 a,b 的共同前缀长度
			var c = numberOfLeadingZeros(a xor b)
			// 如果 c<12 第一级别就不同，不可能是子区域
			if (c < 12) return false
			//c 为 a,b 的共同区域前缀的长度，是12的倍数
			c -= c % 12
			return a shl c != 0L && b shl c == 0L
		}
		
		/**
		 * 一个区域ID的等级，越小表示范围越大。
		 *
		 * @return 该组织节点的等级，范围1~5
		 */
		fun level(id: Long): Int = 5 - numberOfTrailingZeros(id) / 12
	}
	
	/**
	 * 判断这个区域是否是所给区域的子区域
	 *
	 * @param region 用于判断的组织区域的id
	 * @return true如果region是这个区域的子区域，false如果region不是这个区域的子区域
	 */
	fun isInside(region: Long): Boolean = inside(region, this.id)
}
