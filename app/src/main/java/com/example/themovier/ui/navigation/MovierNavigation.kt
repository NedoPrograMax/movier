package com.example.themovier.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.themovier.ui.screens.about.AboutScreen
import com.example.themovier.ui.screens.details.DetailsScreen
import com.example.themovier.ui.screens.home.HomeScreen
import com.example.themovier.ui.screens.login.LoginScreen
import com.example.themovier.ui.screens.search.SearchScreen
import com.example.themovier.ui.screens.splash.SplashScreen
import com.example.themovier.ui.screens.stats.StatsScreen
import com.example.themovier.ui.screens.update.UpdateScreen

@Composable
fun MovierNavigation(
    navController: NavHostController,
    selectImageLauncher: ActivityResultLauncher<Intent>,
    imageState: MutableState<Uri?>,
) {
    NavHost(navController = navController, startDestination = MovierScreens.SplashScreen.name) {

        composable(MovierScreens.SplashScreen.name) {
            SplashScreen(navController)
        }

        composable(MovierScreens.HomeScreen.name) {
            HomeScreen(navController, selectImageLauncher, imageState)
        }

        composable(MovierScreens.LoginScreen.name) {
            LoginScreen(navController)
        }

        composable(MovierScreens.AboutScreen.name) {
            AboutScreen(navController)
        }

        composable(MovierScreens.SearchScreen.name) {
            SearchScreen(navController)
        }

        val detailsRoot = MovierScreens.DetailsScreen.name
        composable("$detailsRoot/{movieId}/{movieType}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                },
                navArgument("movieType") {
                    type = NavType.StringType
                }
            )
        )
        { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            val movieType = backStackEntry.arguments?.getString("movieType")
            if (!movieId.isNullOrBlank() && !movieType.isNullOrBlank()) {
                DetailsScreen(navController, movieId = movieId, movieType = movieType)
            }

        }

        val updateRoot = MovierScreens.UpdateScreen.name
        composable("$updateRoot/{movieId}",
            arguments = listOf(navArgument("movieId") {
                type = NavType.StringType
            }))
        { backStackEntry ->
            backStackEntry.arguments?.getString("movieId").let { movieId ->
                UpdateScreen(navController, movieId)
            }
        }

        composable(MovierScreens.StatsScreen.name) {
            StatsScreen(navController, hiltViewModel())
        }
    }
}