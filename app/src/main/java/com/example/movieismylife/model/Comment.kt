package com.example.movieismylife.model

// 댓글 데이터 모델
data class Comment(
    val content: String = "",
    val createdAt: Long = 0,
    val movieId: String = "",
    val score: Long = 0,
    val userId: String = "",
)