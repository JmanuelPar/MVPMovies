package com.diego.mvpretrosample.network

import com.diego.mvpretrosample.data.NetworkMovieDetail
import com.diego.mvpretrosample.data.NetworkMovies
import com.diego.mvpretrosample.utils.Constants.API_TMDB_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Get your API key here : www.themoviedb.org

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val logger = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(API_TMDB_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface TmdbApiService {

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): NetworkMovies

    @GET("3/movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): NetworkMovieDetail
}

object TmdbApi {
    val retrofitService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}