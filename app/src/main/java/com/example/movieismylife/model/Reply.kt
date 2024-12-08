package com.example.movieismylife.model

import com.google.firebase.Timestamp

// 답글 데이터 모델
data class Reply(
    val userId: String = "",
    val commentId: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp(0, 0)
)