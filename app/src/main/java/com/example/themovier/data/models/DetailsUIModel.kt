package com.example.themovier.data.models

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
)