package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInsertDTO(
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
)