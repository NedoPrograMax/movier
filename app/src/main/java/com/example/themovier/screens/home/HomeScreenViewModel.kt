package com.example.themovier.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.DataOrException
import com.example.themovier.model.MovierUser
import com.example.themovier.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<MovierUser, Exception>> =
       mutableStateOf(DataOrException(null, Exception("")))

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        getUserData(userId)
    }

    private fun getUserData(userId: String){
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getUserInfo(userId)
            loading.value = false
        }
    }


}