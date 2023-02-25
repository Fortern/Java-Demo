package xyz.fortern.config

import com.alibaba.fastjson2.support.spring.data.redis.FastJsonRedisSerializer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import xyz.fortern.constant.CAMERA_CACHE
import java.time.Duration

/**
 * Redis配置类
 */
@Configuration
class RedisConfig: CachingConfigurerSupport() {
	@Bean
	fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
		//Redis模板对象
		val template = RedisTemplate<String, Any>()
		
		//设置连接工厂
		template.setConnectionFactory(redisConnectionFactory)
		
		//设置自定义序列化方式
		//key：字符串类型，使用String的序列化方式
		val stringRedisSerializer = StringRedisSerializer()
		
		//value：Object类型，使用fastjson的序列化方式,直接序列化对象
		val fastJsonRedisSerializer = FastJsonRedisSerializer(Any::class.java)
		
		//指定序列化和反序列化方式
		template.keySerializer = stringRedisSerializer
		template.valueSerializer = fastJsonRedisSerializer
		template.hashKeySerializer = stringRedisSerializer
		template.hashValueSerializer = fastJsonRedisSerializer
		
		//初始化模板
		template.afterPropertiesSet()
		return template
	}
	
	/**
	 * 注入缓存配置cacheManager
	 * 此redis缓存配置会覆盖yml配置文件中的缓存配置
	 */
	@Bean
	fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
		val config = RedisCacheConfiguration.defaultCacheConfig()
		config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
			// 设置value为json序列化
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(FastJsonRedisSerializer(Any::class.java)))
			// 不缓存空值
			.disableCachingNullValues()
			// 设置缓存的默认过期时间 24小时
			.entryTtl(Duration.ofHours(24))
		
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
