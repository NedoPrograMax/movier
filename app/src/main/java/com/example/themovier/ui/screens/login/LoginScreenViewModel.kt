package com.example.themovier.ui.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.fold

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface Route {
    object Login : Route
    object Home : Route
    object Settings : Route
}

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
) : ViewModel() {
    private val _userCreationError = MutableLiveData<Exception>()
    val userCreationError: LiveData<Exception> get() = _userCreationError

    val loading = MutableStateFlow(false)

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {
        userDataSource.createUserWithEmailAndPassword(
            email = email,
            password = password,
            home = home,
        ).fold(
            { userDataSource.createUserItem(email, it.toString()) },
            { _userCreationError.value = it }
        )
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
