package com.example.themovier.ui.screens.update


import com.example.themovier.ui.models.UpdateModel

data class UpdateState(
    val loading : Boolean = false,
    val data: UpdateModel? = null,
    val note: String? = null,
    val updateType: UpdateType = UpdateType.Details,
)
