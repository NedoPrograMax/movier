package com.example.themovier.ui.screens.login

import com.github.michaelbull.result.Result

sealed interface LoginAction {
    data class ExceptionSignUp(val result: Result<Unit, Exception>) : LoginAction
    data class ExceptionLogIn(val result: Result<Unit, Exception>) : LoginAction
}