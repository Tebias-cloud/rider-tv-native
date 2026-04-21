package com.rider.tv.data

import com.rider.tv.data.model.Category
import com.rider.tv.data.model.Stream
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("player_api.php")
    suspend fun getLiveCategories(
        @Query("username") user: String,
        @Query("password") pass: String,
        @Query("action") action: String = "get_movie_categories"
    ): List<Category>

    @GET("player_api.php")
    suspend fun getLiveStreams(
        @Query("username") user: String,
        @Query("password") pass: String,
        @Query("action") action: String = "get_movie_streams"
    ): List<Stream>

    companion object {
        private const val USER_AGENT = "MoviePlatform/1.0"

        fun create(baseUrl: String): MovieApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl.let { if (it.endsWith("/")) it else "$it/" })
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiService::class.java)
        }
    }
}
