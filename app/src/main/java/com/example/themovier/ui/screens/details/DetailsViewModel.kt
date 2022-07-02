package com.example.themovier.ui.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.model.Episode
import com.example.themovier.model.MovierItem
import com.example.themovier.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    var data: MovierItem? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)

  /*  init {
        searchMovies("Friends")
    }

   */

    fun searchMovie(movieId: String, movieType: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (movieId.isBlank()) return@launch
            try {
                if(movieType == "movie") {
                    val response = repository.getMovieInfo(movieId)
                    if (response.data != null) {
                        response.data?.apply {

                            var genress = ""
                            for (genre in genres){
                                genress += genre.name + ", "
                            }
                            genress = genress.subSequence(0, genress.length - 2).toString()

                            data = MovierItem(
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
                        isLoading = false
                    }
                }
                else  if(movieType == "tv") {
                    val response = repository.getTvInfo(movieId)
                    if (response.data != null) {
                        response.data?.apply {

                            var genress = ""
                            for (genre in genres){
                                genress += genre.name + ", "
                            }
                            genress = genress.subSequence(0, genress.length - 2).toString()

                            data = MovierItem(
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
                        isLoading = false
                    }
                }
            }catch (e:Exception){
                Log.e("DetailsViewModel", e.message!!)
            }
        }
    }
}