package xyz.fortern.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.fortern.constant.REGION_CACHE
import xyz.fortern.mapper.RegionMapper
import xyz.fortern.pojo.user.Region

@Service
@CacheConfig(cacheNames = [REGION_CACHE])
class RegionService(
	val regionMapper: RegionMapper,
) {
	@Lazy
	@Autowired
	private lateinit var regionService: RegionService
	
	/**
	 * 根据ID获取Region
	 */
	@Cacheable(key = "#id", unless = "#result == null")
	fun getById(id: Int): Region {
		return regionMapper.selectById(id)
	}
	
	/**
	 * 删除一个组织节点
	 */
	@CacheEvict(key = "#id")
	fun deleteById(id: Int): Boolean {
		return regionMapper.deleteById(id) > 0
	}
	
	/**
	 * 更新一条区域信息
	 */
	@CachePut(key = "#region.id", unless = "#result == true")
	fun update(region: Region): Boolean {
		return regionMapper.updateById(region) > 0
	}
	
	/**
	 * 添加一个新区域，会查找其父节点下可用的最小的ID，作为该区域的ID
	 */
	@Transactional
	fun insert(name: String, parentId: Long?) {
	
	}
}
