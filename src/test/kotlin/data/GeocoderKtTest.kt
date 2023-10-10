package data

import com.ap.SS.BuildConfig
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GeocoderKtTest {

    @Test
    fun getLatitudeAndLongitude() = runTest {
        val expected: Pair<String, String> = Pair("38.0284528", "-93.234697")
        val address = "12523 US-65 Cross Timbers Missouri 65634"
        val apiKey = BuildConfig.GOOGLE_MAPS_KEY
        assertEquals(expected, getLatitudeAndLongitude(address, apiKey))
    }
}