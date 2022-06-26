package com.example.themovier.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import com.example.themovier.model.BottomNavItem
import com.example.themovier.navigation.MovierScreens

object Constants {
  //  http://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "9babceb540db351c661df28d7549673b"

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