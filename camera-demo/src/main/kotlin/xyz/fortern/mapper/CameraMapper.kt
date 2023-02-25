package xyz.fortern.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import xyz.fortern.pojo.OnvifCamera

@Repository
interface CameraMapper {
	
	/**
	 * 从数据库中查询一个摄像头信息
	 *
	 * @param id 摄像头的id
	 * @return 摄像头详情，如果不存在则为null
	 */
	@Select("select * from camera where id = #{id}")
	fun getById(id: Int): OnvifCamera?
	
	/**
	 * 向数据库中添加一个摄像头信息
	 *
	 * @param camera 摄像头详情
	 */
	@Insert("insert into camera (ip, port, username, password) VALUES (#{ip}, #{port}, #{username}, #{password})")
	@Options(keyProperty = "id", keyColumn = "id", useGeneratedKeys = true)
	fun insert(camera: OnvifCamera): Int
	
	/**
	 * 更新摄像头信息
	 *
	 * @param camera 摄像头详情
	 */
	@Update("update camera set ip=#{ip}, port=#{port}, username=#{username}, password=#{password} where id=#{id}")
	fun updateById(camera: OnvifCamera)
	
	/**
	 * 从数据库中删除一条摄像头信息
	 *
	 * @param id 摄像头的id
	 */
	@Delete("delete from camera where id = #{id}")
	fun deleteById(id: Int): Int
	
}