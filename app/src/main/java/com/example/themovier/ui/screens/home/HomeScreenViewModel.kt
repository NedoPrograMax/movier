package com.example.themovier.ui.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.domain.user.UserDataSource
import com.example.themovier.ui.models.HomeUIModel
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val movieDataSource: MovieDataSource,
) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private val _action = MutableSharedFlow<HomeAction>()
    val action: SharedFlow<HomeAction> get() = _action


    init {
        getUserData()
        getUserMovies()
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetUserData -> getUserData()
            is HomeIntent.GetUserMovies -> getUserMovies()
            is HomeIntent.UpdateUserProfileData -> updateUserProfileData(intent.map)
            is HomeIntent.PutImage -> putImage(intent.uri)
        }
    }

    private fun getUserData(userId: String = currentUserId) =
        viewModelScope.launch {
            setLoading(true)
            userDataSource.getUserInfo(userId).fold(
                { dataUser ->
                    _state.emit(_state.value.copy(dataUser = dataUser))
                },
                { it.printStackTrace() }
            )

            setLoading(false)
        }

    private fun getUserMovies(userId: String = currentUserId) =
        viewModelScope.launch {
            setLoading(true)
            movieDataSource.getUserMovies(userId).onSuccess { dataMovies ->
                _state.emit(_state.value.copy(
                    dataMovies = dataMovies.map { movierItem ->
                        HomeUIModel(
                            id = movierItem.id,
                            startDate = movierItem.startDate,
                            finishDate = movierItem.finishDate,
                            posterUrl = movierItem.posterUrl
                        )
                    }
                ))
            }
            setLoading(false)
        }

    private fun updateUserProfileData(
        map: Map<String, Any>,
        userId: String = currentUserId,
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            val result = userDataSource.updateUserProfileData(map, userId)
            _action.emit(HomeAction.ExceptionUpdateSharedFlow(result))

        }

    private fun putImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _action.emit(HomeAction.UriUpdateSharedFlow(userDataSource.putImage(uri)))
        }
    }

    private fun setLoading(value: Boolean) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = value))
        }
    }


}
