package com.example.themovier.data.utils

import com.example.themovier.ui.models.DetailsUIModel
import com.example.themovier.data.models.Episode
import com.example.themovier.ui.models.UpdateUiModel
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovierItemModel
import com.example.themovier.domain.models.TvDetails

fun MovieDetails.toDetails(): DetailsUIModel = this.run {
    DetailsUIModel(
        description = overview + "\n" + "\n",
        genres = genres,
        language = original_language,
        status = status,
        title = title,
        releaseDate = release_date,
        posterUrl = poster_path,
    )
}


fun TvDetails.toDetails() = this.run {
    DetailsUIModel(
        description = overview + "\n" + "\n"
                + "Last Air Date: " + last_air_date + "\n" + "\n"
                + "Number of Episodes: " + number_of_episodes.toString() + "\n" + "\n"
                + "Number of Seasons: " + number_of_seasons.toString() + "\n",
        genres = genres,
        posterUrl = poster_path,
        language = original_language,
        status = status,
        title = original_name,
        releaseDate = first_air_date,
        seasons = seasons.map {
            Episode(
                season = it.season_number,
                episode = it.episode_count
            )
        }.filter {
            it.season > 0
        }
            .sortedBy { it.season },
    )
}


fun MovierItemModel.toUpdateModel() = this.run {
    UpdateUiModel(
        note = note,
        resource = resource,
        season = season,
        episode = episode,
        rating = rating,
        userId = userId,
        type = type,
        startDate = startDate,
        finishDate = finishDate,
        favoriteEpisodes = favoriteEpisodes,
        idDb = idDb,
        id = id,
    )
}