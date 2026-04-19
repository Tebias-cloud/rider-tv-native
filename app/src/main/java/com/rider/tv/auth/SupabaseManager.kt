package com.rider.tv.auth

import io.github.janitenert.supabase.SupabaseClient
import io.github.janitenert.supabase.createSupabaseClient
import io.github.janitenert.supabase.gotrue.GoTrue
import io.github.janitenert.supabase.postgrest.Postgrest

object SupabaseManager {
    private const val SUPABASE_URL = "https://eckvrldsejxpcpkufyhq.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVja3ZybGRzZWp4cGNwa3VmeWhxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU4NjAzNTMsImV4cCI6MjA5MTQzNjM1M30.A-hoyiKQENZFkh_x1MoFmKqwgzNzkqbYikUvjhGt7G8"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
    }

    // Auth methods would go here
}
