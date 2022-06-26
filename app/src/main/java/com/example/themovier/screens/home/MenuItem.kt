package com.example.themovier.screens.home

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem (
    val id: String,
    val title: String,
    val contentDescription: String = "Icon",
    val icon: ImageVector
        )