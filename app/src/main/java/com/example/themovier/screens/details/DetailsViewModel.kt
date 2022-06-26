package com.example.themovier.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.model.MovieDetails
import com.example.themovier.model.MovieFromApi
import com.example.themovier.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    var data: MovieDetails? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)

  /*  init {
        searchMovies("Friends")
    }

   */

    fun searchMovie(movieId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (movieId.isBlank()) return@launch
            try {
                val response = repository.getMovieInfo(movieId)
                if (response.data != null){
                    data = response.data
                    isLoading = false
                }
            }catch (e:Exception){
                Log.e("DetailsViewModel", e.message!!)
            }
        }
    }
}