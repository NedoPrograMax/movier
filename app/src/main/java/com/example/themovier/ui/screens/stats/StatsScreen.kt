package com.example.themovier.ui.screens.stats

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
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
        if (viewModel.loading.value && viewModel.dataMovies.value.isNullOrEmpty()) {
            LinearProgressIndicator()
        } else {
            StatsContent(navController = navController, viewModel)
        }
    }
}

@Composable
fun StatsContent(navController: NavController, viewModel: HomeScreenViewModel) {

    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels / displayMetrics.density
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val cardModifier = Modifier
        .width((screenWidth * 0.45).dp)
        .height((screenHeight * 0.3).dp)

    val watchedMovieList = viewModel.dataMovies.value?.filter { movierItem ->
        movierItem.finishDate.isNotBlank()
    }!!

    MovieItemsRow(
        movieList = watchedMovieList,
        cardModifier = cardModifier,
        screenHeight = screenHeight,
        navController = navController,
        screenWidth = screenWidth
    )
}