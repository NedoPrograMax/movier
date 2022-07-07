package com.example.themovier.data.models

import com.example.themovier.domain.models.Genre
import com.google.firebase.firestore.Exclude

data class MovierItem(
    val title: String = "",
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
    @Exclude
    var seasons: List<Episode> = listOf(),
    @Exclude
    var description: String = "",
    @Exclude
    var genres: List<Genre> = listOf(),
    @Exclude
    var status: String = "",
    @Exclude
    var language: String = "",
    @Exclude
    var releaseDate: String = "",
)


