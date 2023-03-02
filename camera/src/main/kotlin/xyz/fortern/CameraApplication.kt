package xyz.fortern

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("xyz.fortern.mapper")
class CameraApplication

fun main(args: Array<String>) {
	runApplication<CameraApplication>(*args)
}
