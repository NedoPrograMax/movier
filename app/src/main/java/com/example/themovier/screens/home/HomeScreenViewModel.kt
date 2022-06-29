package com.example.themovier.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.DataOrException
import com.example.themovier.model.MovierItem
import com.example.themovier.model.MovierUser
import com.example.themovier.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    var loadingUser = mutableStateOf(false)
    val dataUser: MutableState<DataOrException<MovierUser, Exception>> =
       mutableStateOf(DataOrException(null, Exception("")))

    var loadingMovies = mutableStateOf(false)
    val dataMovies: MutableState<DataOrException<List<MovierItem>, Exception>> =
        mutableStateOf(DataOrException(null, Exception("")))

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        getUserData(userId)
        getUserMovies(userId)
    }

    private fun getUserData(userId: String){
        viewModelScope.launch {
            loadingUser.value = true
            dataUser.value = repository.getUserInfo(userId)
            loadingUser.value = false
        }
    }

     fun getUserMovies(userId: String){
        viewModelScope.launch {
            loadingMovies.value = true
            dataMovies.value = repository.getUserMovies(userId)
            loadingMovies.value = false
        }
    }


}