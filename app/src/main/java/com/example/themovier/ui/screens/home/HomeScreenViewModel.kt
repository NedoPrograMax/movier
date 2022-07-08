package com.example.themovier.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.models.HomeUIModel
import com.example.themovier.data.models.MovierUserModel
import com.example.themovier.domain.repositories.FireRepo
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepo) : ViewModel() {
    var loadingUser = mutableStateOf(false)
    val dataUser: MutableState<MovierUserModel?> =
        mutableStateOf(null)

    var loadingMovies = mutableStateOf(false)
    val dataMovies: MutableState<List<HomeUIModel>?> =
        mutableStateOf(listOf())

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        getUserData(userId)
        getUserMovies(userId)
    }

    private fun getUserData(userId: String) {
        viewModelScope.launch {
            loadingUser.value = true
            repository.getUserInfo(userId).onSuccess {
                dataUser.value = it
            }
            loadingUser.value = false
        }
    }

    fun getUserMovies(userId: String) {
        viewModelScope.launch {
            loadingMovies.value = true
            repository.getUserMovies(userId).onSuccess {
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
    }


}