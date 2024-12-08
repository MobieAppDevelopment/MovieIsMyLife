package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Movies
import com.example.movieismylife.response.ApiService
import com.example.movieismylife.response.MovieDetailResponse
import com.example.movieismylife.response.MovieResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailViewModel : ViewModel() {
    private val _popularMovieList = mutableStateOf<List<Movies>>(mutableListOf<Movies>())
    val popularMovieList : State<List<Movies>> = _popularMovieList

    private val _searchMovieList = mutableStateOf<List<Movies>>(mutableListOf<Movies>())
    val searchMovieList : State<List<Movies>> = _searchMovieList

    private val _genreMap = mutableStateOf<Map<Int, String>>(mutableMapOf())
    val genreMap : State<Map<Int, String>> = _genreMap

    val _movieDetail = mutableStateOf<MovieDetailResponse?>(null);

    private val BASE_URL = "https://api.themoviedb.org/3/"

    // 웹브라우저 창 열기
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 어떤 주소를 입력했다!
    private val api = retrofit.create(ApiService::class.java)

    fun fetchMovieDetail(movieid:Int = 912649) {
        viewModelScope.launch {
            try {
                api.getDetailMovie(movieid).enqueue(object : Callback<MovieDetailResponse> {
                    override fun onResponse(
                        call: Call<MovieDetailResponse>,
                        response: Response<MovieDetailResponse>
                    ) {
                        if (response.isSuccessful) {
                            val movieDetail = response.body()
                            Log.d("checking", "%%%%%%%%${movieDetail}")
                            if (movieDetail != null) {
                                _movieDetail.value = movieDetail
                            }
                        } else {
                            // Handle error
                            Log.d("checking", "%%%%%%%%")
                        }
                    }

                    override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                        // Handle error
                        Log.d("checking", "&&&&&&&&")
                    }
                })
            } catch (e: Exception) {
                println("오류가 발생한 것 같아요~~")
                e.printStackTrace()
            }
        }
    }
}