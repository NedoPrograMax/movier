package com.example.themovier.ui.screens.details

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.domain.models.MovierItemModel
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.widgets.MovieDescription
import com.example.themovier.ui.widgets.MovierAppBar
import com.example.themovier.ui.widgets.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    navController: NavController,
    movieId: String?,
    movieType: String,
) {

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var isFavoriteIcon by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    var movierId = ""

    Log.d("navcont", navController.backQueue.toString())


    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Details",
                icon = Icons.Default.ArrowBack,
                onIconClick = {
                    navController.popBackStack()
                },
                actions = {
                    IconButton(onClick = {
                        if (!isFavoriteIcon) {
                            viewModel.data?.apply {
                                val movie = MovierItemModel(
                                    idDb = movieId?.toInt()!!,
                                    type = movieType,
                                    posterUrl = posterUrl,
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                viewModel.createMovie(movie)
                                showToast(context, "Movie was added to your list")
                            }
                        } else {
                            if (movierId.isNotBlank()) viewModel.deleteMovie(movierId)
                        }
                        isFavoriteIcon = !isFavoriteIcon
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Icon",
                            tint = if (isFavoriteIcon) Color.Red else Color.LightGray
                        )
                    }
                }
            )
        }

    ) { padding ->
        padding
        viewModel.searchMovie(movieId!!, movieType)
        if (viewModel.isLoading) {
            LinearProgressIndicator()

        } else {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("idDb", movieId.toInt())
                .get()
                .addOnSuccessListener { document ->
                    val movieList = document.toObjects(MovierItemModel::class.java)
                    if (movieList.any {
                            Log.d("CheckSmth", it.id)
                            movierId = it.id
                            it.userId == currentUserId && it.type == movieType
                        }) {
                        isFavoriteIcon = true
                    }
                }
            DetailsContent(viewModel = viewModel)
        }
    }
}

@Composable
fun DetailsContent(viewModel: DetailsViewModel) {
    val context = LocalContext.current

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            viewModel.data?.apply {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://image.tmdb.org/t/p/w500$posterUrl")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    contentDescription = "Movie Image"
                )

                Text(
                    text = title,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )

                MovieDescription()


            }
        }
    }
}


