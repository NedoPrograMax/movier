package com.example.themovier.ui.screens.home

import android.net.Uri
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface HomeAction {
    data class UriUpdateSharedFlow(val result: Result<Uri, Exception>) : HomeAction

    data class ExceptionUpdateSharedFlow(val result: Result<Unit, Exception>) : HomeAction
}
