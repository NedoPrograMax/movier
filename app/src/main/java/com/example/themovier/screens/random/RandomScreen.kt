package com.example.themovier.screens.random

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.themovier.navigation.MovierScreens

@Composable
fun RandomScreen(navController: NavController){
    Text(text = MovierScreens.RandomScreen.name)
}