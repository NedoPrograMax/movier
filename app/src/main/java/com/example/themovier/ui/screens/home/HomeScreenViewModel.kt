package com.example.themovier.ui.screens.home

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.ui.models.HomeUIModel
import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val movieDataSource: MovieDataSource,
) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

 //   var loadingUser = mutableStateOf(false)
    val dataUser: MutableState<MovierUserModel?> =
        mutableStateOf(null)

    val loading = mutableStateOf(false)

  //  var loadingMovies = mutableStateOf(false)
    val dataMovies: MutableState<List<HomeUIModel>> =
        mutableStateOf(listOf())

    private val _uriUpdateSharedFlow = MutableSharedFlow<Result<Uri, Exception>>()
    val uriUpdateSharedFlow = _uriUpdateSharedFlow.asSharedFlow()

    private val _exceptionUpdateSharedFlow = MutableSharedFlow<Result<Unit, Exception>>()
    val exceptionUpdateSharedFlow = _exceptionUpdateSharedFlow.asSharedFlow()

    init {
        getUserData()
        getUserMovies()
    }

    private fun getUserData(userId: String = currentUserId) =
        viewModelScope.launch {
            loading.value = true
            userDataSource.getUserInfo(userId).fold(
                { dataUser.value = it },
                { it.printStackTrace() }
            )
            loading.value = false
        }

    fun getUserMovies(userId: String = currentUserId) =
        viewModelScope.launch {
            loading.value = true
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
            loading.value = false
        }

    fun updateUserProfileData(
        map: Map<String, Any>,
        userId: String = currentUserId,
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            val result = userDataSource.updateUserProfileData(map, userId)
            _exceptionUpdateSharedFlow.emit(result)
        }

    fun putImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _uriUpdateSharedFlow.emit(userDataSource.putImage(uri))
        }
    }


}
