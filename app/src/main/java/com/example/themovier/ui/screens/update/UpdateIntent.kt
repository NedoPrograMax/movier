package com.example.themovier.ui.screens.update

import com.example.themovier.data.models.Comment
import com.example.themovier.domain.models.TotalMovie

sealed class UpdateIntent {
    data class GetMovie(val movieId: String) : UpdateIntent()
    data class DeleteMovie(val movieId: String) : UpdateIntent()
    data class SetNote(val note: String) : UpdateIntent()
    data class SetUpdateType(val type: UpdateType) : UpdateIntent()
    data class CreateTotalMovie(val totalMovie: TotalMovie) : UpdateIntent()
    data class GetTotalMovie(val idDb: String, val type: String) : UpdateIntent()
    data class UpdateTotalMovieData(
        val totalMovieHashMap: Map<String, Any>,
        val idDb: String,
        val type: String,
    ) : UpdateIntent()

    data class UpdateCommentList(
        val listComment: Map<String, Comment>,
    ) : UpdateIntent()
}