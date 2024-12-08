package com.example.movieismylife.model

import com.google.firebase.Timestamp

data class CommentView (
    val score: Float = 0f,
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
