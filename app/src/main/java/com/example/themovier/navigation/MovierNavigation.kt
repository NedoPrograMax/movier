package com.example.themovier.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.themovier.screens.about.AboutScreen
import com.example.themovier.screens.details.DetailsScreen
import com.example.themovier.screens.home.HomeScreen
import com.example.themovier.screens.login.LoginScreen
import com.example.themovier.screens.random.RandomScreen
import com.example.themovier.screens.search.SearchScreen
import com.example.themovier.screens.splash.SplashScreen

@Composable
fun MovierNavigation(
    navController: NavHostController,
    selectImageLauncher: ActivityResultLauncher<Intent>,
    imageState: MutableState<Uri?>
) {
    NavHost(navController = navController, startDestination = MovierScreens.SplashScreen.name){

        composable(MovierScreens.SplashScreen.name){
            SplashScreen(navController)
        }

        composable(MovierScreens.HomeScreen.name){
            HomeScreen(navController, selectImageLauncher, imageState)
        }

        composable(MovierScreens.RandomScreen.name){
            RandomScreen(navController)
        }

        composable(MovierScreens.LoginScreen.name){
            LoginScreen(navController)
        }

        composable(MovierScreens.AboutScreen.name){
            AboutScreen(navController)
        }

        composable(MovierScreens.SearchScreen.name){
            SearchScreen(navController)
        }

        val detailsRoot = MovierScreens.DetailsScreen.name
        composable(detailsRoot + "/{movieId}",
        arguments = listOf(navArgument("movieId"){
            type = NavType.StringType
        }))
        {backStackEntry->
            backStackEntry.arguments?.getString("movieId").let {movieId->
                DetailsScreen(navController, movieId)
            }
        }
    }
}