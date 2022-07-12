package com.example.themovier.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.user.UserDataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userDataSource: UserDataSource) :
    ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        home: () -> Unit,
    ) = viewModelScope.launch {
        val userId = async {
            userDataSource.createUserWithEmailAndPassword(
                email = email,
                password = password,
                onFailure = onFailure,
                onSuccess = { userId ->
                    userDataSource.createUserItem(email, userId)
                },
                home = home,
            )
        }

    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        home: () -> Unit,
    ) = viewModelScope.launch {
        userDataSource.signInWithEmailAndPassword(
            email = email,
            password = password,
            onFailure = onFailure,
            home = home
        )
    }

}
