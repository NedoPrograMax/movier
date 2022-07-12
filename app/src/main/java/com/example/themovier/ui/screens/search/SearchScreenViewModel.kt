package com.example.themovier.ui.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.repositories.ApiRepo
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: ApiRepo) : ViewModel() {
    var data: MovieFromApi? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)

    init {
        searchMovies("Friends", "movie")
    }

    fun searchMovies(query: String, movieType: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isBlank()) return@launch
            try {
                val response = repository.getMovies(query = query, movieType = movieType)
                response.onSuccess {
                    data = it
                    isLoading = false
                }

            } catch (e: Exception) {
                Log.e("MovieViewModel", e.message!!)
            }
        }
    }


}
