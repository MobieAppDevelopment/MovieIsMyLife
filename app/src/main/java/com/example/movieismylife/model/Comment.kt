package com.example.movieismylife.model

// 댓글 데이터 모델
data class Comment(
    val userId: String = "",
    val movieId: String = "",
    val content: String = "",
    val createdAt: Long = 0
)