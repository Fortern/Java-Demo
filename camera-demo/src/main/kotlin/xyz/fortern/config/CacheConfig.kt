package xyz.fortern.config

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import xyz.fortern.constant.CAMERA_CACHE
import java.time.Duration

@EnableCaching//打开SpringBoot的Cache自动装配
@Configuration
class CacheConfig {
	/**
	 * 注入缓存配置cacheManager
	 * 此redis缓存配置会覆盖yml配置文件中的缓存配置
	 */
	@Bean
	fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
		
		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
		var config = RedisCacheConfiguration.defaultCacheConfig()
		config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
			// 设置value为json序列化
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericFastJsonRedisSerializer()))
			// 不缓存空值
			.disableCachingNullValues()
			// 设置缓存的默认过期时间
			// entryTtl方法会返回一个新对象，大坑
			.entryTtl(Duration.ofHours(3))
		
		// 特殊缓存空间应用不同配置
		val map: MutableMap<String, RedisCacheConfiguration> = HashMap(2)
		// a-camera 缓存60分钟
		map[CAMERA_CACHE] = config.entryTtl(Duration.ofHours(1))
		
		// 使用自定义的缓存配置初始化一个
		return RedisCacheManager.builder(connectionFactory)
			// 默认配置
			.cacheDefaults(config)
			// 特殊缓存配置
			.withInitialCacheConfigurations(map)
			// 事务
			.transactionAware()
			.build()
	}
}
