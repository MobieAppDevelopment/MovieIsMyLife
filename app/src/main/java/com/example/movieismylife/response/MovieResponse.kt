package com.example.movieismylife.response

import com.example.movieismylife.model.Movies
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    var results: List<Movies>
)
