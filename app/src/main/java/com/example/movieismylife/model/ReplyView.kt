package com.example.movieismylife.model

import com.google.firebase.Timestamp

data class ReplyView (
    val name: String = "",
    val profile: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp(0, 0)
)