package com.example.themovier.ui.screens.update

import com.github.michaelbull.result.Result

sealed interface UpdateAction {
    data class ExceptionCreateTotalMovie(val result: Result<Unit, Exception>) : UpdateAction
}