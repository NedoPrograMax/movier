package com.example.themovier.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.models.MovierUser
import com.example.themovier.data.repo.FireRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepoImpl): ViewModel() {
    var loadingUser = mutableStateOf(false)
    val dataUser: MutableState<MovierUser?> =
       mutableStateOf(null)

    var loadingMovies = mutableStateOf(false)
    val dataMovies: MutableState<List<MovierItem>?> =
        mutableStateOf(listOf())

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        getUserData(userId)
        getUserMovies(userId)
    }

    private fun getUserData(userId: String){
        viewModelScope.launch {
            loadingUser.value = true
            dataUser.value = repository.getUserInfo(userId).getOrNull()
            loadingUser.value = false
        }
    }

     fun getUserMovies(userId: String){
        viewModelScope.launch {
            loadingMovies.value = true
            dataMovies.value = repository.getUserMovies(userId).getOrNull()
            loadingMovies.value = false
        }
    }


}