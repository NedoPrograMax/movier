package com.example.themovier.ui.models

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeScreenMenuItem(
    val id: String,
    val title: String,
    val contentDescription: String = "Icon",
    val icon: ImageVector,
)
