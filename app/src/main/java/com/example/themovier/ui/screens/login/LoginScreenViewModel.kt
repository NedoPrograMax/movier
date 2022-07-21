package com.example.themovier.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.domain.user.UserDataSource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userDataSource: UserDataSource) :
    ViewModel() {
    private val loading

    private val _exceptionSignUpSharedFlow = MutableSharedFlow<Exception>()
    val exceptionSignUpSharedFlow = _exceptionSignUpSharedFlow.asSharedFlow()

    private val _exceptionLogInSharedFlow = MutableSharedFlow<Exception>()
    val exceptionLogInSharedFlow = _exceptionLogInSharedFlow.asSharedFlow()

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val result = async {
            userDataSource.createUserWithEmailAndPassword(
                email = email,
                password = password,
            )
        }
        _exceptionSignUpSharedFlow.emit(result.await())
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val result = async {
            userDataSource.signInWithEmailAndPassword(
                email = email,
                password = password,
            )
        }
        _exceptionLogInSharedFlow.emit(result.await())
    }

}
