package xyz.fortern

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@MapperScan("xyz.fortern.mapper")
@EnableDiscoveryClient
@SpringBootApplication
class CameraDemoApplication

fun main(args: Array<String>) {
	runApplication<CameraDemoApplication>(*args)
}
