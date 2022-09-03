package com.example.themovier.ui.models

import com.example.themovier.data.models.Comment

data class FullComment(
    val comment: Comment = Comment(),
    val userPicture: String = "",
    val userName: String = "",
)
