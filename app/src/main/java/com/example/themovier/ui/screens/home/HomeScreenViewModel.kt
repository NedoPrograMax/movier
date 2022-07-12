package com.example.themovier.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.ui.models.HomeUIModel
import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val movieDataSource: MovieDataSource,
) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    var loadingUser = mutableStateOf(false)
    val dataUser: MutableState<MovierUserModel?> =
        mutableStateOf(null)

    var loadingMovies = mutableStateOf(false)
    val dataMovies: MutableState<List<HomeUIModel>?> =
        mutableStateOf(listOf())

    init {
        getUserData()
        getUserMovies()
    }

    fun getUserData(userId: String = currentUserId) =
        viewModelScope.launch {
            loadingUser.value = true
            userDataSource.getUserInfo(userId).onSuccess {
                dataUser.value = it
            }
            loadingUser.value = false
        }

    fun getUserMovies(userId: String = currentUserId) =
        viewModelScope.launch {
            loadingMovies.value = true
            movieDataSource.getUserMovies(userId).onSuccess {
                dataMovies.value = it.map { movierItem ->
                    HomeUIModel(
                        id = movierItem.id,
                        startDate = movierItem.startDate,
                        finishDate = movierItem.finishDate,
                        posterUrl = movierItem.posterUrl
                    )
                }
            }
            loadingMovies.value = false
        }

    fun updateUserProfileData(
        map: Map<String, Any>,
        userId: String = currentUserId,
        onSuccess: () -> Unit,
    ) =
        viewModelScope.launch {
            userDataSource.updateUserProfileData(map, userId, onSuccess)
        }


}
