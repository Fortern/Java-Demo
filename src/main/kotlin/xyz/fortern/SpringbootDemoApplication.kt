package xyz.fortern

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan("xyz.fortern.dao")
open class SpringbootDemoApplication

// 主函数，启动类
fun main(args: Array<String>) {
	runApplication<SpringbootDemoApplication>(*args)
}

