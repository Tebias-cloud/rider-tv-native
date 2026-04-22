package com.rider.tv.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import com.rider.tv.data.model.ExternalAccount
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SupabaseManager {
    private const val SUPABASE_URL = "https://eckvrldsejxpcpkufyhq.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVja3ZybGRzZWp4cGNwa3VmeWhxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU4NjAzNTMsImV4cCI6MjA5MTQzNjM1M30.A-hoyiKQENZFkh_x1MoFmKqwgzNzkqbYikUvjhGt7G8"

    private val unsafeOkHttpClient: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        httpEngine = OkHttp.create {
            preconfigured = unsafeOkHttpClient
        }
        install(Auth)
        install(Postgrest)
    }

    suspend fun getAnyAccount(): ExternalAccount? {
        return try {
            val response = client.from("external_accounts")
                .select {
                    limit(1)
                }
            android.util.Log.d("SupabaseManager", "FALLBACK RAW DATA: ${response.data}")
            response.decodeSingleOrNull<ExternalAccount>()
        } catch (e: Exception) {
            android.util.Log.e("SupabaseManager", "Error in fallback fetching: ${e.message}", e)
            null
        }
    }

    // User access
    suspend fun getExternalAccount(userId: String): ExternalAccount? {
        return try {
            val response = client.from("external_accounts")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
            
            // Log raw data for debugging
            android.util.Log.d("SupabaseManager", "FETCHING ACCOUNT FOR: $userId")
            android.util.Log.d("SupabaseManager", "RAW DATA: ${response.data}")
            
            val res = response.decodeSingleOrNull<ExternalAccount>()
            android.util.Log.d("SupabaseManager", "DECODED RESULT: $res")
            res
        } catch (e: Exception) {
            android.util.Log.e("SupabaseManager", "Error fetching external account: ${e.message}", e)
            null
        }
    }
}
