package com.example.themovier.ui.screens.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.data.datasource.FirebaseDataSourceImpl
import com.example.themovier.data.models.Episode
import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.utils.formatDate
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.details.DetailsViewModel
import com.example.themovier.ui.widgets.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UpdateScreen(
    navController: NavController,
    movieId: String?,
    viewModel: UpdateViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    var movie by remember {
        mutableStateOf<MovierItem?>(MovierItem())
    }
    val firebaseDataSource = FirebaseDataSourceImpl()

    viewModel.getMovie(movieId!!)
    if (viewModel.data.value == null || viewModel.data.value?.title?.isBlank()!!) {
        LinearProgressIndicator()
    } else {
        movie = viewModel.data.value
        detailsViewModel.searchMovie(movieId = movie!!.idDb.toString(), movieType = movie!!.type)
        if (detailsViewModel.data == null || detailsViewModel.data!!.title.isBlank()) {
            CircularProgressIndicator()
        } else {
            detailsViewModel.data?.apply {
                movie!!.status = status
                movie!!.language = language
                movie!!.description = description
                movie!!.genres = genres
                movie!!.seasons = seasons
                movie!!.releaseDate = releaseDate
            }
            Scaffold(
                topBar = {
                    MovierAppBar(
                        title = "Update",
                        icon = Icons.Default.ArrowBack,
                        onIconClick = { navController.popBackStack() },
                        actions = {
                            IconButton(onClick = {
                                firebaseDataSource.deleteMovie(movieId)
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Icon",
                                    tint = Color.Red.copy(alpha = 0.8f)
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                padding
                movie?.let { UpdateContent(navController, it) }
            }
        }
    }
}

@Composable
fun UpdateContent(navController: NavController, movie: MovierItem) {
    val context = LocalContext.current
    var detailsType by remember {
        mutableStateOf("details")
    }
    LazyColumn(
        //  modifier = Modifier.scrollable(scroll, orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://image.tmdb.org/t/p/w500" + movie.posterUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentDescription = "Movie Image"
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = detailsType == "details",
                        onClick = {
                            detailsType = "details"
                        },
                    )
                    Text(text = "Details")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = detailsType == "update",
                        onClick = {
                            detailsType = "update"
                        },
                    )
                    Text(text = "Update")
                }
            }


            if (detailsType == "details") {
                movie.MovieDescription()
            }


            val noteState = remember {
                mutableStateOf(movie.note)
            }

            val noteChange = remember(noteState.value) {
                noteState.value != movie.note
            }

            var startWatching by remember { mutableStateOf(movie.startDate) }

            var finishWatching by remember { mutableStateOf(movie.finishDate) }

            var someButtonClicked by remember {
                mutableStateOf(false)
            }

            if (detailsType == "update") {
                InputField(
                    valueState = noteState,
                    labelId = "Your notes",
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                    textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            } else {
                Text(
                    text = "Note: " + noteState.value,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Light
                )
            }

            if (startWatching.isBlank()) {
                if (detailsType == "update") {
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(vertical = 6.dp),
                        onClick = {
                            someButtonClicked = true
                            startWatching = formatDate(Timestamp.now())
                        }
                    ) {
                        Text(text = "Start Watching", fontSize = 14.sp)
                    }
                }
            } else {
                Text(text = "Started Watching at: $startWatching", fontSize = 20.sp)

                if (finishWatching.isBlank()) {
                    if (detailsType == "update") {
                        Button(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(vertical = 6.dp),
                            onClick = {
                                someButtonClicked = true
                                finishWatching = formatDate(Timestamp.now())
                            }
                        ) {
                            Text(text = "Finish Watching", fontSize = 14.sp)
                        }
                    }
                } else {
                    Text(text = "Finished Watching at: $finishWatching", fontSize = 20.sp)
                    if (detailsType == "update") {
                        Button(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(vertical = 6.dp),
                            onClick = {
                                someButtonClicked = true
                                startWatching = formatDate(Timestamp.now())
                                finishWatching = ""
                            }
                        ) {
                            Text(text = "Start Watching Again", fontSize = 14.sp)
                        }
                    }
                }
            }


            var season by remember { mutableStateOf(movie.season) }
            val seasonList = generateSequence(1) { it + 1 }.take(movie.seasons.size).toList()
                .map {
                    it.toString()
                }
            var episode by remember { mutableStateOf(movie.episode) }

            var favSeason by remember { mutableStateOf(1) }
            var favEpisode by remember { mutableStateOf(1) }

            val favoriteEpisodes = remember {
                mutableStateOf(movie.favoriteEpisodes)
            }

            if (movie.type == "tv") {
                val episodeList =
                    generateSequence(1) { it + 1 }.take(movie.seasons[season - 1].episode)
                        .toList()
                        .map {
                            it.toString()
                        }

                val favEpisodeList =
                    generateSequence(1) { it + 1 }.take(movie.seasons[favSeason - 1].episode)
                        .toList()
                        .map {
                            it.toString()
                        }

                val showSeasonDialog = remember {
                    mutableStateOf(false)
                }

                val showEpisodeDialog = remember {
                    mutableStateOf(false)
                }

                val showFavSeasonDialog = remember {
                    mutableStateOf(false)
                }

                val showFavEpisodeDialog = remember {
                    mutableStateOf(false)
                }


                Text(text = "You stopped at ")

                Row() {
                    if (showSeasonDialog.value) {
                        ChooseDialog(setShowDialog = { showSeasonDialog.value = it },
                            listOfStrings = seasonList,
                            onItemClick = {
                                season = it.toInt()
                                showSeasonDialog.value = false
                            })
                    }

                    TextButton(
                        onClick = { showSeasonDialog.value = true },
                        enabled = detailsType == "update"
                    ) {
                        Text(text = "Season: " + season)
                    }

                    if (showEpisodeDialog.value) {
                        ChooseDialog(setShowDialog = { showEpisodeDialog.value = it },
                            listOfStrings = episodeList,
                            onItemClick = {
                                episode = it.toInt()
                                showEpisodeDialog.value = false
                            })
                    }

                    TextButton(
                        onClick = { showEpisodeDialog.value = true },
                        enabled = detailsType == "update") {
                        Text(text = "Episode: " + episode)
                    }
                }

                if (showFavSeasonDialog.value) {
                    ChooseDialog(setShowDialog = { showFavSeasonDialog.value = it },
                        listOfStrings = seasonList,
                        onItemClick = {
                            favSeason = it.toInt()
                            showFavSeasonDialog.value = false
                            showFavEpisodeDialog.value = true
                        })
                }

                if (showFavEpisodeDialog.value) {
                    ChooseDialog(setShowDialog = { showFavEpisodeDialog.value = it },
                        listOfStrings = favEpisodeList,
                        onItemClick = {
                            if (detailsType == "update") {
                                favEpisode = it.toInt()
                                showFavEpisodeDialog.value = false

                                favoriteEpisodes.value = favoriteEpisodes.value.plus((Episode(
                                    season = favSeason,
                                    episode = favEpisode
                                )))
                            }
                        })
                }

                if (detailsType == "update") {
                    TextButton(onClick = {
                        showFavSeasonDialog.value = true
                        if (favSeason != 1 && favEpisode != 1) {

                        }
                    }) {
                        Text(text = "Add Favorite Episode")
                    }
                }

                FavoriteEpisodes(favoriteEpisodes) {
                    favoriteEpisodes.value = favoriteEpisodes.value.minus(it)
                }
            }
            val resourceState = remember {
                mutableStateOf(movie.resource)
            }

            if (detailsType == "update") {
                InputField(valueState = resourceState,
                    labelId = "Resource of your movie",
                    modifier = Modifier.fillMaxWidth(0.8f))
            } else if (movie.resource.isNotBlank()) {
                val uriHandler = LocalUriHandler.current

                TextButton(onClick = { uriHandler.openUri(movie.resource) }) {
                    Text(
                        text = "Get to the resource",
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }

            if (detailsType == "update") {

                Button(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(0.4f),
                    enabled = noteChange || someButtonClicked || movie.season != season || movie.episode != episode
                            || movie.favoriteEpisodes != favoriteEpisodes.value.toList()
                            || resourceState.value != movie.resource,
                    onClick = {
                        FirebaseFirestore.getInstance().collection("movies")
                            .document(movie.id)
                            .update(
                                hashMapOf(
                                    "note" to noteState.value,
                                    "startDate" to startWatching,
                                    "finishDate" to finishWatching,
                                    "season" to season,
                                    "favoriteEpisodes" to favoriteEpisodes.value,
                                    "resource" to resourceState.value,
                                    "episode" to episode
                                ) as Map<String, Any>
                            )
                            .addOnSuccessListener {
                                if (finishWatching.isBlank()) {
                                    navController.navigate(MovierScreens.HomeScreen.name) {
                                        popUpTo(MovierScreens.UpdateScreen.name) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    navController.navigate(MovierScreens.StatsScreen.name) {
                                        popUpTo(MovierScreens.UpdateScreen.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                    }
                ) {
                    Text(text = "Update", fontSize = 18.sp)
                }
            }
        }
    }
}



