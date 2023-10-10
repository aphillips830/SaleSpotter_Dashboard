import com.ap.SS.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

abstract class DBConnection {
    companion object {
        private const val URL = BuildConfig.SUPABASE_URL
        private const val KEY = BuildConfig.SUPABASE_ANON_KEY

        val client = createSupabaseClient(
            supabaseUrl = URL,
            supabaseKey = KEY
        ) {
            install(Postgrest)
        }
    }
}