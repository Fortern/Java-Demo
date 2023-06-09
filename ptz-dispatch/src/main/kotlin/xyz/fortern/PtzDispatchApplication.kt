package xyz.fortern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PtzDispatchApplication

fun main(args: Array<String>) {
	runApplication<PtzDispatchApplication>(*args)
}
