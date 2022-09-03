package com.example.themovier.domain.models

import com.example.themovier.data.models.Comment

data class TotalMovie(
    val idDb: String = "",
    val type: String = "movie",
    val comments: Map<String, Comment> = mapOf(),
    val rating: Double = 0.0,
)
