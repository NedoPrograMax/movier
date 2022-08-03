package com.example.themovier.ui.screens.update

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.ui.models.UpdateModel
import com.example.themovier.data.utils.toUpdateModel
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.ui.screens.home.HomeIntent
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val movieDataSource: MovieDataSource) :
    ViewModel() {
    var state by mutableStateOf(UpdateState())
        private set

    fun processIntent(intent: UpdateIntent) {
        when (intent) {
            is UpdateIntent.GetMovie -> getMovie(intent.movieId)
            is UpdateIntent.DeleteMovie -> deleteMovie(intent.movieId)
            is UpdateIntent.SetNote -> setNote(intent.note)
            is UpdateIntent.SetUpdateType -> setUpdateType(intent.type)
        }
    }

    private fun getMovie(movieId: String) =
        viewModelScope.launch {
            state = state.copy(loading = true)
            movieDataSource.getMovie(movieId).onSuccess {
                state = state.copy(data = it.toUpdateModel())
                if (state.data != null) {
                    state = state.copy(loading = false)
                    return@launch
                }
            }
        }

    private fun deleteMovie(movieId: String) =
        viewModelScope.launch {
            movieDataSource.deleteMovie(movieId)
        }

    private fun setNote(note: String) {
        Log.d("NoteTag", note)
        state = state.copy(note = note)
    }

    private fun setUpdateType(type: UpdateType) {
        state = state.copy(updateType = type)
    }
}

