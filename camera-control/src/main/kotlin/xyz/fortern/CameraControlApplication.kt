package xyz.fortern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ["xyz.fortern.api"])
class CameraControlApplication

fun main(args: Array<String>) {
	runApplication<CameraControlApplication>(*args)
}
