package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id")
    val userID: Long,
    @SerialName("name")
    val name: String,
    @SerialName("address")
    val address: String,
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String,
    @SerialName("postcode")
    val postcode: String,
    @SerialName("email")
    val email: String,
    @SerialName("created_at")
    val joinedDate: String
)
