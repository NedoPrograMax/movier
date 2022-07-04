package com.example.themovier.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.domain.models.Movie
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.widgets.InputField
import com.example.themovier.ui.widgets.MovierAppBar

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchScreenViewModel = hiltViewModel()
){

    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Search Screen",
                icon = Icons.Default.ArrowBack,
                onIconClick = {
                    navController.navigate(MovierScreens.HomeScreen.name) {
                        popUpTo(MovierScreens.SearchScreen.name) {
                            inclusive = true
                        }
                    }
                })
        }
    ) {
        it
        SearchContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun SearchContent(navController: NavController, viewModel: SearchScreenViewModel) {
    val searchQuery = rememberSaveable{
        mutableStateOf("")
    }
    var movieType by remember{
        mutableStateOf("movie")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputField(
            valueState = searchQuery,
            labelId = "Movie Title",
            modifier = Modifier.fillMaxWidth(0.9f),
            imeAction = ImeAction.Search,
            onAction = KeyboardActions {
                viewModel.searchMovies(searchQuery.value, movieType = movieType)
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(  verticalAlignment = Alignment.CenterVertically) {
               RadioButton(
                   selected = movieType == "movie",
                   onClick = { movieType = "movie"
                       viewModel.searchMovies(searchQuery.value.ifEmpty { "Friends" }, movieType = movieType)
                             },
               )
                Text(text = "Movie")
            }

            Row(  verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = movieType == "tv",
                    onClick = { movieType = "tv"
                        viewModel.searchMovies(searchQuery.value.ifEmpty { "Friends" }, movieType = movieType)
                              },
                )
                Text(text = "TV Show")
            }
        }
        
        if (viewModel.isLoading){
            LinearProgressIndicator()
        }
        else{
            MoviesColumn(viewModel = viewModel, navController, movieType = movieType)
        }
        

    }
}

@Composable
fun MoviesColumn(viewModel: SearchScreenViewModel, navController: NavController, movieType: String){
    val movieList = viewModel.data!!.results
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ){
        items(movieList){movie->
            MovieCardInGrid(movie, navController, movieType = movieType)
        }
    }
}

@Composable
fun MovieCardInGrid(movie: Movie, navController: NavController, movieType: String) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { navController.navigate(MovierScreens.DetailsScreen.name + "/${movie.id}" + "/${movieType}") },
                contentScale = ContentScale.Fit,
                contentDescription = "Movie Image"
            )
        }

