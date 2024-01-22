package xyz.fortern

import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import xyz.fortern.mapper.RegionMapper
import xyz.fortern.pojo.user.Region
import java.lang.Long.numberOfTrailingZeros
import java.util.*

/**
 * Region - service
 */
open class RegionService(
	private val regionMapper: RegionMapper
) {
	/**
	 * 获取某一区域下的直接子区域列表
	 *
	 * @param parentId 父节点的ID
	 */
	@Transactional(readOnly = true)
	open fun selectListById(parentId: Long?): List<Region> {
		val id = parentId ?: 0L
		val level = numberOfTrailingZeros(id) / 12
		var bit = level * 12
		if (bit == 0)
			return Collections.emptyList()
		val id2 = (id ushr bit + 1) shl bit
		bit = (6 - level) * 12 + 4
		return regionMapper.selectListById(id, id2, bit)
	}
	
	/**
	 * 获取某一区域下最小的没有使用的ID
	 *
	 * @param parentId 父节点的ID
	 */
	private fun getMinIdInRegion(parentId: Long?): Long? {
		val regionId = parentId ?: 0
		val bit = numberOfTrailingZeros(regionId) / 12 * 12 - 12
		val result = regionMapper.getMinIdInRegion(regionId, bit)
		    /* 第一种情况，这是表中第一个区域；第二种情况，父区域不存在或父区域下节点已满 */
			?: return if (regionId == 0L) 281474976710656L else null

		return if (regionId == 0L) {
			/* 第三种情况，正常返回；第四种情况，第一级子区域已满，返回null；第五种情况，父级子区域已满 */
			if (result ushr 60 == 0L) result else null
		} else {
			/* 第四种情况，正常返回；第五种情况，父级子区域已满 */
			if (Region.inside(result, regionId)) result else null
		}
	}
	
	/**
	 * 创建一个新区域
	 *
	 * @param parentId 父区域的ID
	 * @param name 新建区域的名字
	 * @param code 第三方区域ID
	 * @return 新建的组织区域
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	open fun createRegion(parentId: Long?, name: String, code: String?): Region? {
		val id = getMinIdInRegion(parentId) ?: return null
		val bit = numberOfTrailingZeros(id) / 12 * 12
		val id2 = (id ushr bit + 1) shl bit
		val maxId = regionMapper.selectMaxSortById(id, id2)
		val region = Region(id, name)
		region.code = code
		region.sort = maxId + 1
		val i = regionMapper.insertNew(region)
		return if (i == 1) region else null
	}
	
	/**
	 * 删除一个区域
	 *
	 * @param id 区域的ID
	 * @return 删除是否成功
	 */
	@Transactional
	open fun deleteRegion(id: Long): Boolean {
		val bit = numberOfTrailingZeros(id) / 12 * 12
		return regionMapper.deleteByParentId(id, bit)
	}
	
}
