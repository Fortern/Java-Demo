package xyz.fortern.predicate

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory
import org.springframework.web.server.ServerWebExchange
import java.util.function.Predicate

/**
 * Session 断言工厂
 */
class SessionRoutePredicateFactory :
	AbstractRoutePredicateFactory<SessionRoutePredicateFactory.Config>(Config::class.java) {
	
	override fun apply(config: Config?): Predicate<ServerWebExchange> {
		TODO("Not yet implemented")
	}
	
	class Config {
	
	}
}