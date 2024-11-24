package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Movies
import com.example.movieismylife.response.ApiService
import com.example.movieismylife.response.GenreResponse
import com.example.movieismylife.response.MovieResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieListViewModel : ViewModel() {
    private val _popularMovieList = mutableStateOf<List<Movies>>(mutableListOf<Movies>())
    val popularMovieList : State<List<Movies>> = _popularMovieList

    private val _isPopularMoviesFetched = MutableStateFlow(false)
    val isPopularMoviesFetched: StateFlow<Boolean> = _isPopularMoviesFetched

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
        if (_isPopularMoviesFetched.value) {
            Log.d("MovieListViewModel", "Popular movies already fetched, skipping.")
            return // Don't fetch if already fetched
        }

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
                            if (!movies.isNullOrEmpty()) {
                                _popularMovieList.value = movies
                                _isPopularMoviesFetched.value = true

                                // Log the fetched movies
                                Log.d("MovieListViewModel", "Fetched ${movies.size} popular movies:")
                                movies.forEachIndexed { index, movie ->
                                    Log.d("MovieListViewModel", "${index + 1}. ${movie.title} (ID: ${movie.id})")
                                }
                            } else {
                                Log.w("MovieListViewModel", "영화 리스트가 비어있습니다.")
                            }
                        } else {
                            Log.e("MovieListViewModel", "populur 영화 api를 호출하는 과정에서 오류가 발생했습니다. ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        Log.e("MovieListViewModel", "인기있는 영화를 가져오는 것을 실패했습니다.", t)
                    }
                })
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "인기있는 영화를 가져오던 중에 예기치 못한 에러가 발생했습니다.", e)
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

    // 랜덤한 영화 하나를 가져오는 함수
    fun getRandomMovie(): Movies? {
        return popularMovieList.value.randomOrNull()
    }

}