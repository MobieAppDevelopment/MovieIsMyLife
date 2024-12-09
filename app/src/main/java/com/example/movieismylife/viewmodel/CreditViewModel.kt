package com.example.movieismylife.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieismylife.model.Credits
import com.example.movieismylife.model.Movies
import com.example.movieismylife.response.ApiService
import com.example.movieismylife.response.CreditsResponse
import com.example.movieismylife.response.MovieResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreditViewModel : ViewModel() {

    private val _creditList = mutableStateOf<List<Credits>>(mutableListOf<Credits>())
    val creditList : State<List<Credits>> = _creditList

//    private val _isPopularMoviesFetched = MutableStateFlow(false)
//    val isPopularMoviesFetched: StateFlow<Boolean> = _isPopularMoviesFetched

//    private val _genreMap = mutableStateOf<Map<Int, String>>(mutableMapOf())
//    val genreMap : State<Map<Int, String>> = _genreMap

    private val BASE_URL = "https://api.themoviedb.org/3/"

    // 웹브라우저 창 열기
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 어떤 주소를 입력했다!
    private val api = retrofit.create(ApiService::class.java)

    // 인기 있는 영화 20개를 popularMovieList에 가져온다
    fun fetchCast(movieId: Int) {
//        if (_isPopularMoviesFetched.value) {
//            Log.d("MovieListViewModel", "Popular movies already fetched, skipping.")
//            return // Don't fetch if already fetched
//        }

        viewModelScope.launch {
            try {
                api.getCredits(movieId).enqueue(object : Callback<CreditsResponse> {
                    override fun onResponse(
                        call: Call<CreditsResponse>,
                        response: Response<CreditsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val cast = data?.cast
                            if (!cast.isNullOrEmpty()) {
                                _creditList.value = cast
//                                _isPopularMoviesFetched.value = true

                                // Log the fetched movies
                                Log.d("CreditViewModel", "Fetched ${cast.size} popular movies:")
                                cast.forEachIndexed { index, cast ->
                                    Log.d("MovieListViewModel", "${index + 1}. name: ${cast.name}")
                                }
                            } else {
                                Log.w("CreditViewModel", "캐스트가 비어있습니다.")
                            }
                        } else {
                            Log.e("CreditViewModel", "캐스트 api를 호출하는 과정에서 오류가 발생했습니다. ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {
                        Log.e("CreditViewModel", "캐스트를 가져오는 것을 실패했습니다.", t)
                    }
                })
            } catch (e: Exception) {
                Log.e("CreditViewModel", "캐스트를 가져오던 중에 예기치 못한 에러가 발생했습니다.", e)
            }
        }
    }
}