package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.response.MovieResponse
import com.example.movieismylife.model.Movies
import com.example.movieismylife.response.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieListViewModel : ViewModel() {
    // 기존의 MutableList<Movies>
    val movieList = mutableListOf<Movies>()

    // MutableLiveData로 감싸기
    val liveMovieList = MutableLiveData<List<Movies>>(movieList)

    private val BASE_URL = "https://api.themoviedb.org/3/"

    // 웹브라우저 창 열기
    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // 어떤 주소를 입력했다!
    val api = retrofit.create(ApiService::class.java)

    // 입력한 주소 중에 하나로 연결 시도!
    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                api.getPopularMovies().enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(
                        call: Call<MovieResponse>,
                        response: Response<MovieResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val movies = data?.results
                            Log.d("Debugging", "!!!${movies}")
                            if (!movies.isNullOrEmpty()) {
                                movies?.forEach {
                                    movieList.add(it)
                                    Log.d("Debugging", "@@@@${it}")
                                    Log.d("movieList", "@@@@$$$$####${movieList.get(0).adult}")
                                }
                            }
                            response.body()?.results?.forEach { movie ->
                                Log.d("MovieTitle", "Movie: ${movie.title}")
                            }
                        } else {
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        // Handle error
                    }
                })
            } catch (e: Exception) {
                // 오류 처리
            }
        }
    }
}