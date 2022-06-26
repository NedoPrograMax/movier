package com.example.themovier.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.model.MovieFromApi
import com.example.themovier.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {
    var data: MovieFromApi? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)

    init {
        searchMovies("Friends")
    }

     fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isBlank()) return@launch
            try {
                val response = repository.getMovies(query)
                if (response.data != null){
                    data = response.data
                    isLoading = false
                }
            }catch (e:Exception){
                Log.e("MovieViewModel", e.message!!)
            }
        }
    }


}