package com.example.themovier.ui.screens.update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.models.Comment
import com.example.themovier.data.utils.toUpdateModel
import com.example.themovier.domain.models.TotalMovie
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.domain.user.UserDataSource
import com.example.themovier.ui.models.FullComment
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val movieDataSource: MovieDataSource,
    private val userDataSource: UserDataSource,
) :
    ViewModel() {
    private val _state = MutableStateFlow(UpdateState())
    val state: StateFlow<UpdateState> get() = _state

    private val _action = MutableSharedFlow<UpdateAction>()
    val action: SharedFlow<UpdateAction> get() = _action

    fun processIntent(intent: UpdateIntent) {
        when (intent) {
            is UpdateIntent.GetMovie -> getMovie(intent.movieId)
            is UpdateIntent.DeleteMovie -> deleteMovie(intent.movieId)
            is UpdateIntent.SetNote -> setNote(intent.note)
            is UpdateIntent.SetUpdateType -> setUpdateType(intent.type)
            is UpdateIntent.CreateTotalMovie -> createTotalMovie(intent.totalMovie)
            is UpdateIntent.GetTotalMovie -> getTotalMovie(idDb = intent.idDb, type = intent.type)
            is UpdateIntent.UpdateTotalMovieData -> updateTotalMovieData(
                totalMovieHashMap = intent.totalMovieHashMap,
                idDb = intent.idDb,
                type = intent.type)
            is UpdateIntent.UpdateCommentList -> updateCommentList(intent.listComment)
        }
    }

    private fun getMovie(movieId: String) =
        viewModelScope.launch {
            setLoading(true)
            movieDataSource.getMovie(movieId).onSuccess {
                _state.emit(_state.value.copy(data = it.toUpdateModel()))
                if (_state.value.data != null) {
                    getTotalMovie(idDb = it.idDb.toString(), type = it.type)
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

    private fun createTotalMovie(
        totalMovie: TotalMovie,
    ) = viewModelScope.launch(Dispatchers.IO) {
        setLoading(true)
        val result = movieDataSource.createTotalMovie(
            totalMovie
        )
        _action.emit(UpdateAction.ExceptionCreateTotalMovie(result))
        setLoading(false)
    }

    private fun getTotalMovie(idDb: String, type: String) =
        viewModelScope.launch {
            setLoading(true)
            movieDataSource.getTotalMovie(idDb = idDb, type = type).onSuccess { movie ->
                val newData =
                    _state.value.data?.copy(comments = movie.comments.mapValues { FullComment(comment = it.value) },
                        totalRating = movie.rating)

                _state.emit(_state.value.copy(data = newData))

                updateCommentList(movie.comments)

                if (_state.value.data != null) {
                    setLoading(false)
                    getUsersInfo()
                    return@launch
                }
            }.onFailure {
                Log.e("UpdateViewModel", "Getting total Movie: ${it.message}")
                setLoading(false)
            }
        }

    private fun getUsersInfo() =
        viewModelScope.launch {
            setLoading(true)
            userDataSource.getUsersInfo(
                _state.value.data?.comments?.map { comment ->
                    comment.value.comment.authorId
                }!!).fold(
                { dataUsers ->
                    val comments = _state.value.data?.comments
                    val usersInfoList = dataUsers.associateBy { it.userId }
                    val fullComments = comments?.mapValues { entry ->
                        entry.value.copy(
                            userName = usersInfoList[entry.key]?.name!!,
                            userPicture = usersInfoList[entry.key]?.profileUrl!!
                        )
                    }

                    val newData = _state.value.data?.copy(comments = fullComments!!)

                    _state.emit(_state.value.copy(data = newData))
                },
                { it.printStackTrace() }
            )

            setLoading(false)
        }

    private fun updateTotalMovieData(
        totalMovieHashMap: Map<String, Any>,
        idDb: String,
        type: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDataSource.updateTotalMovieData(totalMovieHashMap, idDb, type)
            Log.d("Wooor", "MMM")
        }
    }

    private fun updateCommentList(list: Map<String, Comment>) {
        viewModelScope.launch {
            setLoading(true)
            Log.d("Weeer", list.toString())
            _state.emit(_state.value.copy(listComment = list))
            setLoading(false)
        }
    }

}



