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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.themovier.data.models.Episode
import com.example.themovier.data.utils.formatDate
import com.example.themovier.ui.models.UpdateModel
import com.example.themovier.ui.models.toDetails
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.details.DetailsViewModel
import com.example.themovier.ui.widgets.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UpdateScreen(
    viewModel: UpdateViewModel = hiltViewModel(),
    navController: NavController,
    movieId: String?,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsState()

    viewModel.processIntent(UpdateIntent.GetMovie(movieId!!))
    if (state.data == null || state.data?.type?.isBlank()!!) {
        AnimatedShimmer(navController = navController,screen = MovierScreens.UpdateScreen)
    } else {
        val movie = state.data
        detailsViewModel.searchMovie(movieId = movie!!.idDb.toString(), movieType = movie.type)
        if (detailsViewModel.data == null || detailsViewModel.data!!.title.isBlank()) {
            AnimatedShimmer(navController = navController,screen = MovierScreens.UpdateScreen)
        } else {
            detailsViewModel.data?.apply {
                movie.status = status
                movie.language = language
                movie.description = description
                movie.genres = genres
                movie.seasons = seasons
                movie.releaseDate = releaseDate
                movie.posterUrl = posterUrl
            }
            if (state.note == null) {
                viewModel.processIntent(UpdateIntent.SetNote(movie.note))
            }
            Scaffold(
                topBar = {
                    MovierAppBar(
                        title = "Update",
                        icon = Icons.Default.ArrowBack,
                        onIconClick = { navController.popBackStack() },
                        actions = {
                            IconButton(onClick = {
                                viewModel.processIntent(UpdateIntent.DeleteMovie(movieId))
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
               movie.let { UpdateContent(viewModel, navController, it) }
            }
        }
    }
}

@Composable
fun UpdateContent(viewModel: UpdateViewModel, navController: NavController, movie: UpdateModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    LazyColumn(
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
                        selected = state.updateType == UpdateType.Details,
                        onClick = {
                            viewModel.processIntent(UpdateIntent.SetUpdateType(UpdateType.Details))
                        },
                    )
                    Text(text = "Details")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = state.updateType == UpdateType.Update,
                        onClick = {
                            viewModel.processIntent(UpdateIntent.SetUpdateType(UpdateType.Update))
                        },
                    )
                    Text(text = "Update")
                }
            }


            if (state.updateType == UpdateType.Details) {
                movie.toDetails().MovieDescription()
            }

            var startWatching by rememberSaveable { mutableStateOf(movie.startDate) }

            var finishWatching by rememberSaveable { mutableStateOf(movie.finishDate) }

            var someButtonClicked by rememberSaveable {
                mutableStateOf(false)
            }

            if (state.updateType == UpdateType.Update) {
                InputFieldNote(
                    value = state.note,
                    onValueChange = { viewModel.processIntent(UpdateIntent.SetNote(it)) },
                    labelId = "Your notes",
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                    textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            } else {
                Text(
                    text = "Note: " + state.note,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Light
                )
            }

            if (startWatching.isBlank()) {
                if (state.updateType == UpdateType.Update) {
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
                    if (state.updateType == UpdateType.Update) {
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
                    if (state.updateType == UpdateType.Update) {
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
            var episode by rememberSaveable { mutableStateOf(movie.episode) }

            var favSeason by rememberSaveable { mutableStateOf(1) }
            var favEpisode by rememberSaveable { mutableStateOf(1) }

            val favoriteEpisodes = rememberSaveable {
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

                Row {
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
                        enabled = state.updateType == UpdateType.Update
                    ) {
                        Text(text = "Season: $season")
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
                        enabled = state.updateType == UpdateType.Update) {
                        Text(text = "Episode: $episode")
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
                            if (state.updateType == UpdateType.Update) {
                                favEpisode = it.toInt()
                                showFavEpisodeDialog.value = false

                                favoriteEpisodes.value = favoriteEpisodes.value.plus((Episode(
                                    season = favSeason,
                                    episode = favEpisode
                                )))
                            }
                        })
                }

                if (state.updateType == UpdateType.Update) {
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

            if (state.updateType == UpdateType.Update) {
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

            if (state.updateType == UpdateType.Update) {

                Button(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(0.4f),
                    enabled =   state.note != movie.note || someButtonClicked || movie.season != season || movie.episode != episode
                            || movie.favoriteEpisodes != favoriteEpisodes.value.toList()
                            || resourceState.value != movie.resource,
                    onClick = {
                        FirebaseFirestore.getInstance().collection("movies")
                            .document(movie.id)
                            .update(
                                hashMapOf(
                                    "note" to state.note,
                                    "startDate" to startWatching,
                                    "finishDate" to finishWatching,
                                    "season" to season,
                                    "favoriteEpisodes" to favoriteEpisodes.value,
                                    "resource" to resourceState.value,
                                    "episode" to episode,
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

@Composable
fun FakeUpdateScreen(){

}



