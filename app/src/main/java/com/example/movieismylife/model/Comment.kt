package com.example.movieismylife.model

import com.google.firebase.Timestamp

// 댓글 데이터 모델
data class Comment(
    val userId: String = "",
    val movieId: String = "",
    val score: Float = 0f,
    val content: String = "",
//    val name: String = "",
//    val profile: String = "",
//    val title: String = "",
//    val posterImage: String = "",
    val createdAt: Timestamp = Timestamp(0, 0), // 왜 초기화를 안시키면 안되는가, 무조건 초기화해야되나?
    val movieTitle: String = "",
    val moviePoster: String = "",
)