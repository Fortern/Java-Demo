package xyz.fortern.pojo

data class User(
	val id: Int,
	val name: String,
	val password: String,
	val permission: List<String>
)
