package com.example.themovier.ui.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.datasource.FirebaseDataSourceImpl
import com.example.themovier.data.mappers.MovieMapper
import com.example.themovier.data.models.MovierItem
import com.example.themovier.domain.repositories.ApiRepo
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: ApiRepo) : ViewModel() {
    var data: MovierItem? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)
    private val firebaseDataSource = FirebaseDataSourceImpl()

    /*  init {
          searchMovies("Friends")
      }

     */

    fun searchMovie(movieId: String, movieType: String) {
        Log.d("DetailsViewModel", "$movieId $movieType")
        viewModelScope.launch(Dispatchers.Default) {
            if (movieId.isBlank()) return@launch
            try {
                if (movieType == "movie") {
                    val response = repository.getMovieInfo(movieId)
                    response.onSuccess { result ->
                        if (result.title.isNotBlank()) {
                            data = MovieMapper().toMovierItem(result)
                            Log.d("DetailsViewModel", data.toString())
                            isLoading = false
                        }
                    }
                } else if (movieType == "tv") {
                    val response = repository.getTvInfo(movieId)
                    response.onSuccess { result ->
                        if (result.original_name.isNotBlank()) {
                            data = MovieMapper().toMovierItem(result)
                            Log.d("DetailsViewModel", data.toString())
                            isLoading = false
                        }
                    }
                        .onFailure {
                            Log.d("DetailsViewModel", it.message!!)
                        }
                }
            } catch (e: Exception) {
                Log.e("DetailsViewModel", e.message!!)
            }
        }
    }

    fun createMovie(movie: MovierItem) {
        firebaseDataSource.createMovie(movie)
    }

    fun deleteMovie(movieId: String) {
        firebaseDataSource.deleteMovie(movieId)
    }
}
