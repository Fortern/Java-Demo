package xyz.fortern.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import xyz.fortern.pojo.user.Region

@Repository
interface RegionMapper {
	@Results(
		id = "map", value = [
			Result(column = "id", property = "id"),
			Result(column = "name", property = "name"),
			Result(column = "code", property = "code"),
		]
	)
	@Select("select id, name, code from region where id = #{id};")
	fun selectById(id: Int): Region
	
	@ResultMap("map")
	@Select("select id, name, code from region")
	fun selectAll(): List<Region>
	
	@Insert("insert into region (id, name, code) values (#{id}, #{name}, #{code})")
	fun insertNewRegion(region: Region): Long
	
	@Delete("delete from region where id=#{id}")
	fun deleteById(id: Int): Long
	
	@Update("update region set name = #{name}, code = #{code} where id = #{id}")
	fun updateById(region: Region): Long
}