package com.example.themovier.ui.screens.random

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovier.ui.screens.search.SearchScreenViewModel

@Composable
fun RandomScreen(navController: NavController, viewModel: SearchScreenViewModel = hiltViewModel()){
    val applicationContext = LocalContext.current
    val ai: ApplicationInfo = applicationContext.packageManager
        .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
    val value = ai.metaData["keyValue"]

    if (viewModel.isLoading){
        LinearProgressIndicator()
    }
    else {
        Text(text = viewModel.data.toString())
    }
}