package xyz.fortern.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/file")
class FileController {
	private val logger: Logger = LoggerFactory.getLogger(this::class.java)
	
	@PostMapping("/upload")
	fun getFile(one: MultipartFile): ResponseEntity<String> {
		logger.info("文件size：${one.size}")
		val bytes = ByteArray(10)
		one.inputStream.read(bytes)
		logger.info(bytes.contentToString())
		return ResponseEntity.ok("成功")
	}
}
