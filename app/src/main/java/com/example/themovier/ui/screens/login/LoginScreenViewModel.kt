package com.example.themovier.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userDataSource: UserDataSource) :
    ViewModel() {
    private val _exceptionSignUpSharedFlow = MutableSharedFlow<Result<Unit, Exception>>()
    val exceptionSignUpSharedFlow = _exceptionSignUpSharedFlow.asSharedFlow()

    private val _exceptionLogInSharedFlow = MutableSharedFlow<Result<Unit, Exception>>()
    val exceptionLogInSharedFlow = _exceptionLogInSharedFlow.asSharedFlow()

    var loading by
    mutableStateOf(false)


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        loading = true
        val result = userDataSource.createUserWithEmailAndPassword(
            email = email,
            password = password,
        )
        _exceptionSignUpSharedFlow.emit(result)
        loading = false
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        loading = true
        val result = userDataSource.signInWithEmailAndPassword(
            email = email,
            password = password,
        )
        _exceptionLogInSharedFlow.emit(result)
        loading = false
    }

}
