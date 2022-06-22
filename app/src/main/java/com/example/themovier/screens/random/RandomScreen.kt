package com.example.themovier.screens.random

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.themovier.navigation.MovierScreens

@Composable
fun RandomScreen(navController: NavController){
    val applicationContext = LocalContext.current
    val ai: ApplicationInfo = applicationContext.packageManager
        .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
    val value = ai.metaData["keyValue"]

    Text(text = value.toString())
}