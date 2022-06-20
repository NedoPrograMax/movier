package com.example.themovier.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import com.example.themovier.model.BottomNavItem
import com.example.themovier.navigation.MovierScreens

object Constants {
    val navBottomItemsList = listOf(
        BottomNavItem(
            name = "Home",
            route = MovierScreens.HomeScreen.name,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = "Random",
            route = MovierScreens.RandomScreen.name,
            icon = Icons.Default.Movie
        )
    )
}