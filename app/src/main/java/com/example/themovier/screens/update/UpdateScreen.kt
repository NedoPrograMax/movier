package com.example.themovier.screens.update

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.model.MovierItem
import com.example.themovier.utils.formatDate
import com.example.themovier.widgets.ChooseDialog
import com.example.themovier.widgets.InputField
import com.example.themovier.widgets.MovierAppBar
import com.example.themovier.widgets.showToast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UpdateScreen(navController: NavController, movieId: String?, viewModel: UpdateViewModel = hiltViewModel()){
    var movie by remember{
        mutableStateOf(MovierItem())
    }
    FirebaseFirestore.getInstance().collection("movies")
        .document(movieId!!)
        .get()
        .addOnSuccessListener {document->
             movie = document.toObject(MovierItem::class.java)!!
        }

    if (movie.title.isBlank()){
        LinearProgressIndicator()
    }
    else{
       Scaffold(
           topBar = {
               MovierAppBar(
                   title = "Update",
                   icon = Icons.Default.ArrowBack,
                   onIconClick = {navController.popBackStack()}
               )
           }
       ) {
           it
           UpdateContent(navController = navController, movie = movie)
       }
    }
}

@Composable
fun UpdateContent(navController: NavController, movie: MovierItem){
    val context = LocalContext.current
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



            val noteState = remember{
                mutableStateOf(movie.note)
            }

            val noteChange = remember(noteState.value){
                noteState.value != movie.note
            }

            var startWatching by remember{ mutableStateOf(movie.startDate)}

            var finishWatching by remember{ mutableStateOf(movie.finishDate)}

            var someButtonClicked by remember{
                mutableStateOf(false)
            }

            InputField(
                valueState = noteState,
                labelId = "Your notes",
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )

            if(startWatching.isBlank()){
                Button(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(vertical = 6.dp),
                    onClick = {
                        someButtonClicked = true
                        startWatching = formatDate(Timestamp.now())
                    }
                ) {
                    Text(text = "Start Watching", fontSize = 20.sp)
                }
            }
            else{
                Text(text = "Started Watching at: $startWatching", fontSize = 20.sp)

                if(finishWatching.isBlank()){
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
                else{
                    Text(text = "Finished Watching at: $finishWatching", fontSize = 20.sp)
                }
            }

            val showSeasonDialog = remember{
                mutableStateOf(false)
            }

            val showEpisodeDialog = remember{
                mutableStateOf(false)
            }
            val seasonList = generateSequence(1) { it + 1 }.take(30).toList()
                .map {
                it.toString()
            }
            var season by remember{ mutableStateOf(movie.season)}
            var episode by remember{ mutableStateOf(movie.episode)}

            Row() {


                if (showSeasonDialog.value) {
                    ChooseDialog(setShowDialog = { showSeasonDialog.value = it },
                        listOfStrings = seasonList,
                        onItemClick = {
                            season = it.toInt()
                            showSeasonDialog.value = false
                        })
                }

                TextButton(onClick = { showSeasonDialog.value = true }) {
                    Text(text = "Season: " + season)
                }

                if (showEpisodeDialog.value) {
                    ChooseDialog(setShowDialog = { showEpisodeDialog.value = it },
                        listOfStrings = seasonList,
                        onItemClick = {
                            episode = it.toInt()
                            showEpisodeDialog.value = false
                        })
                }

                TextButton(onClick = { showEpisodeDialog.value = true }) {
                    Text(text = "Episode: " + episode)
                }
            }


            Button(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(0.5f),
                enabled = noteChange || someButtonClicked || movie.season != season || movie.episode != episode,
                onClick = {
                    FirebaseFirestore.getInstance().collection("movies")
                        .document(movie.id)
                        .update(hashMapOf(
                            "note" to noteState.value,
                            "startDate" to startWatching,
                            "finishDate" to finishWatching,
                            "season" to season,
                            "episode" to episode
                        ) as Map<String, Any>)
                        .addOnSuccessListener {
                            navController.popBackStack()
                        }
                }
            ) {
                Text(text = "Update", fontSize = 20.sp)
            }
        }
    }
}