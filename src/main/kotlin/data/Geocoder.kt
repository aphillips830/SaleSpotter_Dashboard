package data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.IllegalStateException

@Serializable
data class GeocoderResponse(
    val results: List<GeocoderResult>,
    val status: String
)

@Serializable
data class GeocoderResult(val geometry: Geometry)

@Serializable
data class Geometry(val location: Location)

@Serializable
data class Location(val lat: String, val lng: String)

suspend fun getLatitudeAndLongitude(address: String, apiKey: String): Pair<String, String> {
    val url = URLBuilder().apply {
        takeFrom("https://maps.googleapis.com/maps/api/geocode/json")
        parameters.append("address", address)
        parameters.append("key", apiKey)
    }.buildString()

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }


    client.use {
        val response: GeocoderResponse = it.get(url).body()
        if (response.results.isNotEmpty()) {
            val result = response.results.first()
            val location = result.geometry.location
            return Pair(location.lat, location.lng)
        }
        else {
            throw IllegalStateException("No geocoding results found for the address: $address. Status code: ${response.status}")
        }
    }
}