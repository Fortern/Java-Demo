package xyz.fortern.controller

import org.apache.rocketmq.client.producer.SendCallback
import org.apache.rocketmq.client.producer.SendResult
import org.apache.rocketmq.spring.core.RocketMQTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rocket")
open class RocketController(
	private val rocketMQTemplate: RocketMQTemplate,
) {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)
	
	@PostMapping("/topic")
	open fun push(@RequestBody params: Map<String, String>): ResponseEntity<String> {
		val message = MessageBuilder.withPayload(params).build()
		logger.info("向主题\"map-topic\"发送消息")
		rocketMQTemplate.send("map-topic", message)
		return ResponseEntity.ok("ok")
	}
	
	@PostMapping("/tag")
	open fun push2(@RequestBody params: Map<String, String>): ResponseEntity<String> {
		logger.info("向主题\"map-topic\"发送带有标签的消息")
		rocketMQTemplate.convertAndSend("map-topic:kotlin-tag", params)
		return ResponseEntity.ok("ok")
	}
	
	@PostMapping("/async")
	open fun push3(@RequestBody params: Map<String, String>): ResponseEntity<String> {
		logger.info("向主题发送异步消息并执行回调")
		rocketMQTemplate.asyncSend("map-topic", params, object : SendCallback {
			override fun onSuccess(sendResult: SendResult) = logger.info("消息发送成功：$sendResult")
			override fun onException(e: Throwable) = logger.error("消息发送失败", e)
		})
		return ResponseEntity.ok("ok")
	}
	
}
