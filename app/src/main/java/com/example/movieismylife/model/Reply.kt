package com.example.movieismylife.model

// 답글 데이터 모델
data class Reply(
    val userId: String = "",
    val content: String = "",
    val createdAt: Long = 0
)