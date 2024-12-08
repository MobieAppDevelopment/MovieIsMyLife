package com.example.movieismylife.model

data class CommentView (
    val score: Long = 0,
    val content: String = "",
    val name: String = "",
    val profile: String = "",
    val title: String = "",
    val posterImage: String = "",
    val createdAt: Long = 0,
    val commentId: String = "",
    val movieId: String = "",
    val userLike: Boolean = false,
    val likeCount: Int = 0
)
