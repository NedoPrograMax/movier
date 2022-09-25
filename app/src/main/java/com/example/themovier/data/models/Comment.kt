package com.example.themovier.data.models

data class Comment(
    val text: String = "",
    val authorId: String = "",
    val likeUsersIdList: List<String> = listOf(),
)
