package xyz.fortern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
open class SpringbootDemoApplication

// 主函数，启动类
fun main(args: Array<String>) {
    runApplication<SpringbootDemoApplication>(*args)
}
