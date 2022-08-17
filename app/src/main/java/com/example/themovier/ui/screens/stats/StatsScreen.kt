package com.example.themovier.ui.screens.stats

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.home.HomeScreenViewModel
import com.example.themovier.ui.widgets.MovieItemsRow
import com.example.themovier.ui.widgets.MovierAppBar

@Composable
fun StatsScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Stats",
                icon = Icons.Default.ArrowBack,
                onIconClick = {
                    navController.navigate(MovierScreens.HomeScreen.name) {
                        popUpTo(MovierScreens.StatsScreen.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    ) {
        it
        if (state.loading && state.dataMovies.isEmpty()) {
            LinearProgressIndicator()
        } else {
            StatsContent(navController = navController, viewModel)
        }
    }
}

@Composable
fun StatsContent(navController: NavController, viewModel: HomeScreenViewModel) {

    val state by viewModel.state.collectAsState()

    val watchedMovieList = state.dataMovies.filter { movierItem ->
        movierItem.finishDate.isNotBlank()
    }!!

    MovieItemsRow(
        movieList = watchedMovieList,
        navController = navController,
    )
}