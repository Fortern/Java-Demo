package xyz.fortern.mapper

import org.apache.ibatis.annotations.*
import org.apache.ibatis.type.JdbcType
import org.springframework.stereotype.Repository
import xyz.fortern.pojo.Camera

@Repository
interface CameraMapper {
	
	/**
	 * 从数据库中查询一个摄像头信息
	 *
	 * @param id 摄像头的id
	 * @return 摄像头详情，如果不存在则为null
	 */
	@Results(
		id = "map", value = [
			Result(id = true, column = "id", property = "id", jdbcType = JdbcType.INTEGER),
			Result(column = "ip", property = "ip", jdbcType = JdbcType.VARCHAR),
			Result(column = "port", property = "port", jdbcType = JdbcType.INTEGER),
			Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
			Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
			Result(column = "region_id", property = "regionId", jdbcType = JdbcType.BIGINT),
			Result(column = "latitude", property = "latitude"),
			Result(column = "longitude", property = "longitude"),
		]
	)
	@Select(
		"select id, ip, port, username, password, region_id, ST_X(coordinate) as latitude, ST_Y(coordinate) as longitude " +
				"from camera where id = #{id}"
	)
	fun selectById(id: Int): Camera?
	
	/**
	 * 向数据库中添加一个摄像头信息
	 *
	 * @param camera 摄像头详情
	 */
	@Insert("insert into camera (ip, port, username, password) VALUES (#{ip}, #{port}, #{username}, #{password})")
	@Options(keyProperty = "id", keyColumn = "id", useGeneratedKeys = true)
	fun insert(camera: Camera): Int
	
	/**
	 * 更新摄像头一般信息
	 *
	 * @param camera 摄像头详情
	 */
	@Update("update camera set ip=#{ip}, port=#{port}, username=#{username}, password=#{password} where id=#{id}")
	fun updateById(camera: Camera): Int
	
	/**
	 * 更新摄像头的坐标信息
	 */
	@Update("update camera set coordinate = ST_GeomFromText('POINT(\${latitude} \${longitude})') where id = #{id}")
	fun updateCoordinateById(
		@Param("id") id: Int,
		@Param("latitude") latitude: Double,
		@Param("longitude") longitude: Double,
	): Int
	
	/**
	 * 从数据库中删除一条摄像头信息
	 *
	 * @param id 摄像头的id
	 */
	@Delete("delete from camera where id = #{id}")
	fun deleteById(id: Int): Int
	
	/**
	 * 将某区域下的摄像机的区域信息清除。
	 */
	@Update("update set region_id = null where region_id=#{regionId}")
	fun removeRegionInfo(regionId: Long): Int
	
}