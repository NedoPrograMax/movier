package com.example.themovier.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.themovier.ui.screens.about.AboutScreen
import com.example.themovier.ui.screens.details.DetailsScreen
import com.example.themovier.ui.screens.home.HomeScreen
import com.example.themovier.ui.screens.login.LoginScreen
import com.example.themovier.ui.screens.search.SearchScreen
import com.example.themovier.ui.screens.splash.SplashScreen
import com.example.themovier.ui.screens.stats.StatsScreen
import com.example.themovier.ui.screens.update.UpdateScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovierNavigation(
    navController: NavHostController,
    selectImageLauncher: ActivityResultLauncher<Intent>,
    imageState: MutableState<Uri?>,
) {
    AnimatedNavHost(navController = navController,
        startDestination = MovierScreens.SplashScreen.name) {

        val uri = "https://www.themovier.com"
        val detailsRoot = MovierScreens.DetailsScreen.name

        composable(
            "$detailsRoot/{movieId}/{movieType}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "$uri/{movieId}/{movieType}" },
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start,
                    animationSpec = tween(1000))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End,
                    animationSpec = tween(1000))
            },
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")
            val type = backStackEntry.arguments?.getString("movieType")
            // Log.d("TAGGRR", id.toString())
            DetailsScreen(
                navController = navController,
                movieId = id,
                movieType = type!!)
        }

        composable(MovierScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(
            MovierScreens.HomeScreen.name,
            enterTransition = {
                when (initialState.destination.route) {
                    MovierScreens.SearchScreen.name -> {
                        slideIntoContainer(AnimatedContentScope.SlideDirection.End,
                            animationSpec = tween(1000))
                    }
                    else -> {
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Start,
                            animationSpec = tween(1000))
                    }
                }
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start,
                    animationSpec = tween(1000))
            },
        ) {
            HomeScreen(
                navController = navController,
                selectImageLauncher = selectImageLauncher,
                imageState = imageState)
        }

        composable(MovierScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(
            MovierScreens.AboutScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End,
                    animationSpec = tween(1000))
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MovierScreens.SearchScreen.name -> {
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Start,
                            animationSpec = tween(1000))
                    }
                    else -> {
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.End,
                            animationSpec = tween(1000))
                    }
                }
            },
        ) {
            AboutScreen(navController = navController)
        }

        composable(
            MovierScreens.SearchScreen.name,
            enterTransition = {
                when (initialState.destination.route) {
                    MovierScreens.HomeScreen.name -> {
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Start,
                            animationSpec = tween(1000))
                    }
                    else -> {
                        slideIntoContainer(AnimatedContentScope.SlideDirection.End,
                            animationSpec = tween(1000))
                    }
                }

            },
            exitTransition = {
                when (targetState.destination.route) {
                    MovierScreens.HomeScreen.name -> {
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.End,
                            animationSpec = tween(1000))
                    }
                    else -> {
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Start,
                            animationSpec = tween(1000))
                    }
                }
            },
        ) {
            SearchScreen(navController = navController)
        }

        val updateRoot = MovierScreens.UpdateScreen.name
        composable("$updateRoot/{movieId}",
            arguments = listOf(navArgument("movieId") {
                type = NavType.StringType
            }),
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End,
                    animationSpec = tween(1000))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start,
                    animationSpec = tween(1000))
            },
        )
        { backStackEntry ->
            backStackEntry.arguments?.getString("movieId").let { movieId ->
                UpdateScreen(navController = navController, movieId = movieId)
            }
        }

        composable(
            MovierScreens.StatsScreen.name,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End,
                    animationSpec = tween(1000))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start,
                    animationSpec = tween(1000))
            },
        ) {
            StatsScreen(navController = navController)
        }
    }
}
