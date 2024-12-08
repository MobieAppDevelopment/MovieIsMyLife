package com.example.movieismylife.model

import com.google.firebase.Timestamp

data class CommentView (
    val score: Long = 0, // Long, 댓글 생성과 관련
    val content: String = "",
    val name: String = "",
    val profile: String = "",
    val title: String = "",
    val posterImage: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val commentId: String = "",
    val movieId: String = "",
    val userLike: Boolean = false,
    val likeCount: Int = 0
)
