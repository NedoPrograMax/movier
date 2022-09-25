package com.example.themovier.ui.models

import com.example.themovier.data.models.Comment
import com.example.themovier.data.models.Episode
import com.example.themovier.domain.models.Genre

data class DetailsUIModel(
    val title: String = "",
    val posterUrl: String = "",
    var seasons: List<Episode> = listOf(),
    var description: String = "",
    var genres: List<Genre> = listOf(),
    var status: String = "",
    var language: String = "",
    var releaseDate: String = "",
    val comments: Map<String, FullComment> = mapOf(),
    val totalRating: Double = 0.0,
)