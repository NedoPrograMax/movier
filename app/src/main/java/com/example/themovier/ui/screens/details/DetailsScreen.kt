package com.example.themovier.ui.screens.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.example.themovier.data.models.MovierItem
import com.example.themovier.ui.widgets.MovierAppBar
import com.example.themovier.ui.widgets.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailsScreen(
    navController: NavController,
    movieId: String?,
    viewModel: DetailsViewModel = hiltViewModel(),
    movieType: String
){

    var isFavoriteIcon by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current

    var movierId = ""


    Scaffold(
        topBar = {
            MovierAppBar(
                title = "Details",
                icon = Icons.Default.ArrowBack,
                onIconClick = {navController.popBackStack()},
                actions = {
                    IconButton(onClick = {
                        if(!isFavoriteIcon){
                            viewModel.data?.apply {
                                val movie: MovierItem = MovierItem(
                                    title = title,
                                    idDb = idDb,
                                    type = movieType,
                                    posterUrl = posterUrl,
                                    description = description,
                                    seasons = seasons,
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                                    )
                                viewModel.createMovie(movie)
                                showToast(context, "$title was added to your list")
                            }
                        }
                        else{
                            if (movierId.isNotBlank())  viewModel.deleteMovie(movierId)
                        }
                        isFavoriteIcon = !isFavoriteIcon
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Icon",
                            tint = if(isFavoriteIcon) Color.Red else Color.LightGray
                        )
                    }
                }
            )
        }

    ) {
        it
        viewModel.searchMovie(movieId!!, movieType)
        if (viewModel.isLoading){
            LinearProgressIndicator()

        }
        else {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("idDb", viewModel.data!!.id)
                .get()
                .addOnSuccessListener { document->
                 val movieList =  document.toObjects(MovierItem::class.java)
                   if(movieList.any {
                           movierId = it.id
                        it.userId == FirebaseAuth.getInstance().currentUser!!.uid
                    }){
                       isFavoriteIcon = true
                   }
                }
            DetailsContent(viewModel = viewModel)
        }
    }
}

@Composable
fun DetailsContent(viewModel: DetailsViewModel){
    val context = LocalContext.current

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://image.tmdb.org/t/p/w500" + viewModel.data?.posterUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentDescription = "Movie Image"
            )

            Text(
                text = viewModel.data!!.title,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = viewModel.data!!.description,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Light
            )


        }
    }
}
