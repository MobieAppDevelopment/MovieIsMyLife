package com.example.movieismylife

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.movieismylife.model.Movies
import com.example.movieismylife.viewmodel.MovieListViewModel

@Composable
fun MainPage(viewModel: MovieListViewModel) {
//    val movies = viewModel.movieList
    val movies = viewModel.movieList

    viewModel.fetchPopularMovies()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Text(text = "Title: ${movies}")
//            Log.d("Debugging", "!!!###${movies}")
//            // LazyColumn 내부에서 items를 사용하여 각 영화의 제목을 Text로 표시
//            items(movies.value) { movie ->
//                Text(text = "Title: ${movie.title}")
//            }
        }
    }
}