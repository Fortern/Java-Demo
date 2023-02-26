package xyz.fortern

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@MapperScan("xyz.fortern.mapper")
@SpringBootApplication
class CameraDemoApplication

fun main(args: Array<String>) {
	runApplication<CameraDemoApplication>(*args)
}
