package com.example.themovier.screens.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.themovier.navigation.MovierScreens

@Composable
fun HomeScreen(navController: NavController){
    Text(text = MovierScreens.HomeScreen.name)
}