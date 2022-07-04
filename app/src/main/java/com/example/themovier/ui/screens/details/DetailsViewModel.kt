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
import com.example.themovier.data.repo.ApiRepoImpl
import com.example.themovier.domain.datasource.FirebaseDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: ApiRepoImpl) : ViewModel() {
    var data: MovierItem? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)
    val firebaseDataSource = FirebaseDataSourceImpl()

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
                    if (response.isSuccess && response.getOrNull() != null) {
                       data = MovieMapper().toMovierItem(response.getOrNull()!!)
                        isLoading = false
                    }
                }
                else  if(movieType == "tv") {
                    val response = repository.getTvInfo(movieId)
                    if (response.isSuccess && response.getOrNull() != null) {
                        data = MovieMapper().toMovierItem(response.getOrNull()!!)
                        isLoading = false
                    }
                }
            }catch (e:Exception){
                Log.e("DetailsViewModel", e.message!!)
            }
        }
    }

    fun createMovie(movie: MovierItem){
        firebaseDataSource.createMovie(movie)
    }

    fun deleteMovie(movieId: String){
        firebaseDataSource.deleteMovie(movieId)
    }
}