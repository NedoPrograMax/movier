package com.example.themovier.domain.models

import com.example.themovier.data.models.Episode

data class MovierItemModel(
    val idDb: Int = 0,
    val id: String = "",
    val posterUrl: String = "",
    val note: String = "",
    val resource: String = "",
    val season: Int = 1,
    val episode: Int = 1,
    val rating: Int = 5,
    val userId: String = "",
    val type: String = "movie",
    val startDate: String = "",
    val finishDate: String = "",
    val favoriteEpisodes: List<Episode> = listOf(),
)


