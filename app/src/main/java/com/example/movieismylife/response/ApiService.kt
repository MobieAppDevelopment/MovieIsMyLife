package com.example.movieismylife.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = "f3be5a17772ae62b309633afd2c3166d", // API_KEY
        @Query("language") language: String = "ko-KR"
    ): Call<MovieResponse>
}