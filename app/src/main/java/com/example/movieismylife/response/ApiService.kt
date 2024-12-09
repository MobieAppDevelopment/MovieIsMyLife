package com.example.movieismylife.response

import com.example.movieismylife.BuildConfig.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR"
    ): Call<MovieResponse>

    @GET("search/movie")
    fun getSearchMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR",
        @Query("include_adult") include_adult: Boolean = true,
        @Query("query") query: String
    ): Call<MovieResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR",
    ): Call<GenreResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path(value = "movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR",
    ): Call<MovieDetailResponse>

    @GET("movie/{movie_id}/credits")
    fun getCredits(
        @Path(value = "movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR",
    ): Call<CreditsResponse>
}