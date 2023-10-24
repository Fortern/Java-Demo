package xyz.fortern.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import xyz.fortern.pojo.user.Region

/**
 * 区域信息 数据库操作
 */
interface RegionMapper {
	/**
	 * 插入新的区域记录
	 */
	@Insert(
		"insert into region (id, name, level, parent, code, sort) " +
				"values (#{id}, #{name}, #{level}, #{parent}, #{code}, #{sort})"
	)
	fun insertNew(region: Region): Int
	
	/**
	 * 获取一个区域的子区域
	 */
	@Select("select id, name, sort, code from region where parent = #{parentId}")
	fun selectChildren(parentId: Long): List<Region>
	
	/**
	 * 获取某一区域内的最大排序值
	 */
	@Select("select max(sort) from region where id > #{idStart} and id < #{idEnd}")
	fun selectMaxSortById(@Param("idStart") idStart: Long, @Param("idEnd") idEnd: Long): Int
	
	/**
	 * 获取某一区域下，从0开始，最小的没有使用的ID
	 *
	 * 比如，假设徐州为 2.1.0.0.0，且徐州下没有设置区域，希望获得睢宁的ID，可以得到 2.1.1.0.0。
	 * 再获取徐州下一个新的ID，可以得到2.1.2.0.0。
	 *
	 * 经过数小时的研究编写出下面的SQL语句。
	 * 这真是天才的设计。
	 */
	@Select(
		"select ((t1.id >> #{bit}) + 1) << #{bit} from region t1 left join region t2 " +
				"on (t1.id >> #{bit}) + 1 = (t2.id >> #{bit}) where t1.parent = #{id} " +
				"and (t1.id >> #{bit} + 12) = #{id} >> #{bit} + 12 limit 1;"
	)
	fun getMinIdInRegion(@Param("id") regionId: Long, @Param("bit") bitToShr: Int): Long?
	
	/**
	 * 删除一个区域下的所有子区域（直接和非直接的）
	 */
	@Delete("delete from region where id >> #{bit} = #{id}")
	fun deleteByParentId(@Param("id") id: Long, @Param("bit") bit: Int): Boolean
}
