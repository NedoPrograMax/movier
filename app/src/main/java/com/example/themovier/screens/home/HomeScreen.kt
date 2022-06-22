package com.example.themovier.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.themovier.model.MovieItem
import com.example.themovier.model.unwatchedMovieList
import com.example.themovier.model.watchedMovieList
import com.example.themovier.navigation.MovierScreens
import com.example.themovier.utils.DragTarget
import com.example.themovier.utils.DropTarget
import com.example.themovier.utils.LongPressDraggable
import com.example.themovier.widgets.AddingArea
import com.example.themovier.widgets.MovieItemsRow
import com.example.themovier.widgets.MovierAppBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navController: NavController){
    Text(text = MovierScreens.HomeScreen.name)
    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Your movie lists",
            actions = {
                IconButton(onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(MovierScreens.LoginScreen.name)
                }) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout Icon")
                }
            })
        }
    ) {
        it
        Column(
            modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
            HomeContent(navController = navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController){

    val isAddingWatched = remember {
        mutableStateOf(false)
    }
    val isAddingUnWatched = remember {
        mutableStateOf(false)
    }
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels / displayMetrics.density
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val cardModifier = Modifier
        .width((screenWidth * 0.45).dp)
        .height((screenHeight * 0.3).dp)

    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        Column() {
            AddingArea(isAddingWatched, )

            MovieItemsRow(
                movieList = watchedMovieList,
                isAdding = isAddingWatched,
                cardModifier = cardModifier,
                screenHeight = screenHeight,
                screenWidth = screenWidth
                )

            AddingArea(isAddingUnWatched)

            MovieItemsRow(
                movieList = unwatchedMovieList,
                cardModifier = cardModifier,
                isAdding = isAddingUnWatched,
                screenHeight = screenHeight,
                screenWidth = screenWidth
            )

        }

    }
}



