package com.example.movieismylife.model

data class CommentView (
    val score: Float = 0f,
    val content: String = "",
    val name: String = "",
    val profile: String = "",
    val title: String = "",
    val posterImage: String = "",
    val createdAt: Long = 0,
    val commentId: String = ""
)
