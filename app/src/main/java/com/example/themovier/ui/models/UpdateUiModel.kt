package com.example.themovier.ui.models

import com.example.themovier.data.models.Comment
import com.example.themovier.data.models.Episode
import com.example.themovier.domain.models.Genre

data class UpdateUiModel(
    val idDb: Int = 0,
    val id: String = "",
    val title: String = "",
    var posterUrl: String = "",
    var seasons: List<Episode> = listOf(),
    var description: String = "",
    var genres: List<Genre> = listOf(),
    var status: String = "",
    var language: String = "",
    var releaseDate: String = "",
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
    val comments: Map<String, FullComment> = mapOf(),
    val totalRating: Double = 0.0,
)

fun UpdateUiModel.toDetails() = this.run {
    DetailsUIModel(
        title = title,
        posterUrl = posterUrl,
        seasons = seasons,
        description = description,
        genres = genres,
        status = status,
        language = language,
        releaseDate = releaseDate,
        comments = comments,
        totalRating = totalRating,
    )
}
