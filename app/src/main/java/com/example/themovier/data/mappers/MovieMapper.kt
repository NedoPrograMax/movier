package com.example.themovier.data.mappers

import com.example.themovier.data.models.Episode
import com.example.themovier.data.models.MovierItem
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.TvDetails

class MovieMapper {
    fun toMovierItem(movieDetails: MovieDetails): MovierItem {
        movieDetails.apply {

            var genress = ""
            for (genre in genres){
                genress += genre.name + ", "
            }
            genress = genress.subSequence(0, genress.length - 2).toString()

            return MovierItem(
                title = title,
                idDb = id,
                posterUrl = poster_path,
                description = overview + "\n" + "\n"
                        + "Genres: " + genress + "\n" + "\n"
                        + "Original Language: " + original_language + "\n" + "\n"
                        + "Status: " + status + "\n" + "\n"
                        + "Released Date: " + release_date + "\n"
            )
        }
    }

    fun toMovierItem(tvDetails: TvDetails): MovierItem{
        tvDetails.apply {
        var genress = ""
        for (genre in genres){
            genress += genre.name + ", "
        }
        genress = genress.subSequence(0, genress.length - 2).toString()

           return MovierItem(
            title = original_name,
            idDb = id,
            posterUrl = poster_path,
            description = overview + "\n" + "\n"
                    + "Genres: " + genress + "\n" + "\n"
                    + "Original Language: " + original_language + "\n" + "\n"
                    + "Status: " + status + "\n" + "\n"
                    + "First Air Date: " + first_air_date + "\n" + "\n"
                    + "Last Air Date: " + last_air_date + "\n" + "\n"
                    + "Number of Episodes: " + number_of_episodes.toString() + "\n" + "\n"
                    + "Number of Seasons: " + number_of_seasons.toString() + "\n",
            seasons = seasons.map {
                Episode(
                    season = it.season_number,
                    episode = it.episode_count
                )
            }.filter {
                it.season > 0
            }
                .sortedBy {
                    it.season
                }
        )
    }
    }
}