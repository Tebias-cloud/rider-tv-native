package com.rider.tv.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import com.rider.tv.data.model.ExternalAccount

object SupabaseManager {
    private const val SUPABASE_URL = "https://eckvrldsejxpcpkufyhq.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVja3ZybGRzZWp4cGNwa3VmeWhxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU4NjAzNTMsImV4cCI6MjA5MTQzNjM1M30.A-hoyiKQENZFkh_x1MoFmKqwgzNzkqbYikUvjhGt7G8"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }

    // User access
    suspend fun getExternalAccount(userId: String): ExternalAccount? {
        return try {
            val res = client.postgrest.from("external_accounts")
                .select(columns = Columns.list("username", "password", "portal_url")) {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeSingleOrNull<ExternalAccount>()
            res
        } catch (e: Exception) {
            null
        }
    }
}
