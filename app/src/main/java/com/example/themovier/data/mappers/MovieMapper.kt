package com.example.themovier.data.mappers

import com.example.themovier.data.models.Episode
import com.example.themovier.data.models.MovierItem
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.TvDetails

class MovieMapper {
    fun toMovierItem(movieDetails: MovieDetails): MovierItem {
        movieDetails.apply {
            return MovierItem(
                title = title,
                idDb = id,
                posterUrl = poster_path,
                description = overview + "\n" + "\n",
                genres = genres,
                language = original_language,
                status = status,
                releaseDate = release_date
            )
        }
    }

    fun toMovierItem(tvDetails: TvDetails): MovierItem {
        tvDetails.apply {

            return MovierItem(
                title = original_name,
                idDb = id,
                posterUrl = poster_path,
                description = overview + "\n" + "\n"
                        + "Last Air Date: " + last_air_date + "\n" + "\n"
                        + "Number of Episodes: " + number_of_episodes.toString() + "\n" + "\n"
                        + "Number of Seasons: " + number_of_seasons.toString() + "\n",
                genres = genres,
                language = original_language,
                status = status,
                releaseDate = first_air_date,
                seasons = seasons.map {
                    Episode(
                        season = it.season_number,
                        episode = it.episode_count
                    )
                }.filter {
                    it.season > 0
                }
                    .sortedBy { it.season }
            )
        }
    }
}
