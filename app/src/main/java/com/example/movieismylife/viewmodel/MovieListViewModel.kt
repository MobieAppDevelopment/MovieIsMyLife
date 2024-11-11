package com.example.movieismylife.viewmodel

import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.response.MovieResponse
import com.example.movieismylife.model.Movies
import com.example.movieismylife.response.ApiService
import com.example.movieismylife.response.GenreResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieListViewModel : ViewModel() {
    private val _popularMovieList = mutableStateOf<List<Movies>>(mutableListOf<Movies>())
    val popularMovieList : State<List<Movies>> = _popularMovieList

    private val _searchMovieList = mutableStateOf<List<Movies>>(mutableListOf<Movies>())
    val searchMovieList : State<List<Movies>> = _searchMovieList

    private val _genreMap = mutableStateOf<Map<Int, String>>(mutableMapOf())
    val genreMap : State<Map<Int, String>> = _genreMap

    private val BASE_URL = "https://api.themoviedb.org/3/"

    // 웹브라우저 창 열기
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 어떤 주소를 입력했다!
    private val api = retrofit.create(ApiService::class.java)

    // 인기 있는 영화 20개를 popularMovieList에 가져온다
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
                                movies.forEach {
                                    _popularMovieList.value += it
                                    Log.d("Debugging", "@@@@${it}")
                                    Log.d("movieList", "@@@@$$$$####${popularMovieList.value[0].adult}")
                                }
                            }
                            response.body()?.results?.forEach { movie ->
                                Log.d("MovieTitle", "Movie: ${movie.title}")
                            }
                        } else {
                            // Handle error
                        }
                        println(popularMovieList.value.size)
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        // Handle error
                    }
                })
            } catch (e: Exception) {
                println("오류가 발생한 것 같아요~~")
                e.printStackTrace()
            }
        }
    }

    // 영화 장르 (ID, NAME)을 genreMap에 가져온다
    fun fetchGenres(){
        viewModelScope.launch {
            try {
                api.getMovieGenres()
                    .enqueue(object : Callback<GenreResponse> {
                        override fun onResponse(
                            call: Call<GenreResponse>,
                            response: Response<GenreResponse>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()
                                val genres = data?.results
                                Log.d("Debugging", "!!!${genres}")
                                if (!genres.isNullOrEmpty()) {
                                    genres.forEach {
                                        _genreMap.value += ((it.id ?: -1) to it.name)
                                    }
                                }
                            } else {
                                // Handle error
                            }
                            println(_genreMap.value.size)
                        }

                        override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                            // Handle error
                        }
                    })
            } catch (e: Exception) {
                println("오류가 발생한 것 같아요~~")
                e.printStackTrace()
            }
        }
    }

    // 검색 결과 20개를 searchMovieList에 가져온다
    fun fetchSearchMovies(query :String, page: Int) {
        _searchMovieList.value = emptyList<Movies>()
        viewModelScope.launch {
            try {
                api.getSearchMovies(query = query, page = page)
                    .enqueue(object : Callback<MovieResponse> {
                        override fun onResponse(
                            call: Call<MovieResponse>,
                            response: Response<MovieResponse>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()
                                val movies = data?.results
                                Log.d("Debugging", "!!!${movies}")
                                if (!movies.isNullOrEmpty()) {
                                    movies.forEach {
                                        _searchMovieList.value += it
                                        Log.d("Debugging", "@@@@${it}")
                                        Log.d("movieList", "@@@@$$$$####${_searchMovieList.value[0].adult}")
                                    }
                                }
                                response.body()?.results?.forEach { movie ->
                                    Log.d("MovieTitle", "Movie: ${movie.title}")
                                }
                            } else {
                                // Handle error
                            }
                            println(_searchMovieList.value.size)
                        }

                        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                            // Handle error
                        }
                    })
            } catch (e: Exception) {
                println("오류가 발생한 것 같아요~~")
                e.printStackTrace()
            }
        }
    }

    // 인기 있는 영화 중 특정 id 장르를 가져온다
    fun getGenreMovies(genre_id:Int) : List<Movies>{
        val genreMovieList : MutableList<Movies> = mutableListOf()
        popularMovieList.value.filter {
            genre_id in (it.genreIds ?: listOf<Int>())
        }.forEach{genreMovieList.add(it)}
        return genreMovieList
    }

}