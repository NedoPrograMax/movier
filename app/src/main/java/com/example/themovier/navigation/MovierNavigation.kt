package com.example.themovier.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.themovier.screens.home.HomeScreen
import com.example.themovier.screens.login.LoginScreen
import com.example.themovier.screens.random.RandomScreen
import com.example.themovier.screens.splash.SplashScreen

@Composable
fun MovierNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MovierScreens.SplashScreen.name){

        composable(MovierScreens.SplashScreen.name){
            SplashScreen(navController)
        }

        composable(MovierScreens.HomeScreen.name){
            HomeScreen(navController)
        }

        composable(MovierScreens.RandomScreen.name){
            RandomScreen(navController)
        }

        composable(MovierScreens.LoginScreen.name){
            LoginScreen(navController)
        }
    }
}