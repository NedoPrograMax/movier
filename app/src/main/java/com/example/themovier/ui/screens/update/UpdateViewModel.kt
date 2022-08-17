package com.example.themovier.ui.screens.update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.utils.toUpdateModel
import com.example.themovier.domain.movie.MovieDataSource
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val movieDataSource: MovieDataSource) :
    ViewModel() {
    private val _state = MutableStateFlow(UpdateState())
    val state: StateFlow<UpdateState> get() = _state

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
            setLoading(true)
            movieDataSource.getMovie(movieId).onSuccess {
                _state.emit(_state.value.copy(data = it.toUpdateModel()))
                if (_state.value.data != null) {
                    setLoading(false)
                    return@launch
                }
            }
        }

    private fun deleteMovie(movieId: String) =
        viewModelScope.launch {
            movieDataSource.deleteMovie(movieId)
        }

    private fun setNote(note: String) {
        viewModelScope.launch {
            Log.d("NoteTag", note)
            _state.emit(_state.value.copy(note = note))
        }
    }

    private fun setUpdateType(type: UpdateType) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(updateType = type))
        }
    }


    private fun setLoading(value: Boolean) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = value))
        }
    }

}

