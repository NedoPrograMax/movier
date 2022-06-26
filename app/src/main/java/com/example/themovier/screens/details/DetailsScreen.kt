package com.example.themovier.screens.details

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovier.widgets.MovierAppBar

@Composable
fun DetailsScreen(
    navController: NavController,
    movieId: String?,
    viewModel: DetailsViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Details",
                icon = Icons.Default.ArrowBack,
                onIconClick = {navController.popBackStack()}
            )
        }

    ) {
        it
        viewModel.searchMovie(movieId!!)
        if (viewModel.isLoading){
            LinearProgressIndicator()
        }
        else {
            DetailsContent(viewModel = viewModel)
        }
    }
}

@Composable
fun DetailsContent(viewModel: DetailsViewModel){

        Text(text = viewModel.data!!.original_title)
}