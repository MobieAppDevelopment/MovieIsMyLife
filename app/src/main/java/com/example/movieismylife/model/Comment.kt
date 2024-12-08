package com.example.movieismylife.model

import com.google.firebase.Timestamp

// 댓글 데이터 모델
data class Comment(
    val userId: String = "",
    val movieId: String = "",
    val score: Long = 0,
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val movieTitle: String = "",
    val moviePoster: String = "",
)