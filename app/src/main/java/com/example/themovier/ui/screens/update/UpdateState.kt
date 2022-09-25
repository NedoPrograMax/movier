package com.example.themovier.ui.screens.update


import com.example.themovier.data.models.Comment
import com.example.themovier.ui.models.UpdateUiModel

data class UpdateState(
    val loading: Boolean = false,
    val data: UpdateUiModel? = null,
    val note: String? = null,
    val updateType: UpdateType = UpdateType.Details,
    val listComment: Map<String, Comment>? = null,
)
