package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Credits
import com.example.movieismylife.model.Movies
import com.example.movieismylife.model.Videos
import com.example.movieismylife.response.ApiService
import com.example.movieismylife.response.CreditsResponse
import com.example.movieismylife.response.MovieDetailResponse
import com.example.movieismylife.response.MovieResponse
import com.example.movieismylife.response.VideoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _videoList = mutableStateOf<List<Videos>>(mutableListOf<Videos>())
    val videoList : State<List<Videos>> = _videoList

    private val _video = MutableStateFlow("")
    val video = _video.asStateFlow()

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

    // 인기 있는 영화 20개를 popularMovieList에 가져온다
    fun fetchVideo(movieId: Int) {
//        if (_isPopularMoviesFetched.value) {
//            Log.d("MovieListViewModel", "Popular movies already fetched, skipping.")
//            return // Don't fetch if already fetched
//        }

        viewModelScope.launch {
            try {
                api.getVideos(movieId).enqueue(object : Callback<VideoResponse> {
                    override fun onResponse(
                        call: Call<VideoResponse>,
                        response: Response<VideoResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val results = data?.results
                            if (!results.isNullOrEmpty()) {
                                _videoList.value = results
                                _video.value = results[0].key
//                                _isPopularMoviesFetched.value = true

                                // Log the fetched movies
                                Log.d("MovieDetailViewModel", "Fetched ${results.size} popular movies:")
                                results.forEachIndexed { index, cast ->
                                    Log.d("MovieDetailViewModel", "${index + 1}. name: ${cast.name}")
                                }
                            } else {
                                Log.w("MovieDetailViewModel", "비디오가 비어있습니다.")
                                _video.value = ""
                            }
                        } else {
                            Log.e("MovieDetailViewModel", "비디오가 api를 호출하는 과정에서 오류가 발생했습니다. ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                        Log.e("MovieDetailViewModel", "비디오를 가져오는 것을 실패했습니다.", t)
                    }
                })
            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "비디오를 가져오던 중에 예기치 못한 에러가 발생했습니다.", e)
            }
        }
    }
}