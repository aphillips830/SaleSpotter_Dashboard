package models

data class User(
    val userID: Long,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val postcode: String,
    val email: String,
    val joinedDate: String
)
