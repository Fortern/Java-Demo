package xyz.fortern.controller

import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/test/mongo")
class MongoController(
	val mongoTemplate: MongoTemplate
) {
	@GetMapping("/test")
	fun test(id: String): ResponseEntity<Any> {
		val query = Query.query(Criteria.where("_id").`is`(id))
		query.fields().include("meta_info.publishers")
		val document = mongoTemplate.findOne(query, Document::class.java, "test")
		val any = document?.getEmbedded(listOf("meta_info", "publishers"), Object::class.java)
		if (any != null) {
			document["publishers"] = any
			document.remove("meta_info")
		}
		return ResponseEntity.ok(document)
	}
}