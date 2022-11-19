package xyz.fortern.listener

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener
import org.apache.rocketmq.spring.core.RocketMQListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

const val groupName2 = "kotlin-consumer2"

@Component
@RocketMQMessageListener(topic = "map-topic", consumerGroup = groupName2, selectorExpression = "kotlin-tag")
class RocketTagListener : RocketMQListener<Map<String, String>> {
	private val logger = LoggerFactory.getLogger(this::class.java)
	
	override fun onMessage(message: Map<String, String>) {
		logger.info("消费消息：$message")
	}
}