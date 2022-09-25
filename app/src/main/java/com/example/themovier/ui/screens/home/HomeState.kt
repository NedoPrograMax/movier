package com.example.themovier.ui.screens.home

import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.ui.models.HomeUIModel

data class HomeState(
    val dataUser: MovierUserModel? = null,
    val loading: Boolean = false,
    val dataMovies: List<HomeUIModel> = listOf(),
)
