package com.example.movieismylife.model

import com.google.firebase.Timestamp

// 댓글 데이터 모델
data class Comment(
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val movieId: String = "",
    val score: Long = 0,
    val userId: String = "",
)