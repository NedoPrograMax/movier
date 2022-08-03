package com.example.themovier.ui.screens.update

import android.net.Uri

sealed class UpdateIntent {
    data class GetMovie(val movieId: String) : UpdateIntent()
    data class DeleteMovie(val movieId: String) : UpdateIntent()
    data class SetNote(val note: String) : UpdateIntent()
    data class SetUpdateType(val type: UpdateType) : UpdateIntent()
}