package com.example.themovier.ui.widgets

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.model.Episode
import com.example.themovier.model.MovierItem
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.home.HomeScreenViewModel
import com.example.themovier.utils.DragTarget
import com.example.themovier.utils.DropTarget
import com.example.themovier.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

@Composable
fun AddingArea(isAdding: MutableState<Boolean>, viewModel: HomeScreenViewModel, title: String) {

    val context = LocalContext.current

    DropTarget<MovierItem>(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) { isInBound, movierItem ->
        val bgColor = if (!isAdding.value)
            Color.Transparent
        else  if (isInBound) {
            Color.Red
        } else {
            Color.White
        }

        movierItem?.let {
            val date = formatDate(Timestamp.now())
            if (isInBound) {

                val hashMap = if(movierItem.startDate.isBlank()) hashMapOf<String, Any>("startDate" to date)
                else hashMapOf<String, Any>("startDate" to "")
                FirebaseFirestore.getInstance().collection("movies")
                    .document(movierItem.id)
                    .update(hashMap)
                    .addOnSuccessListener {
                        Log.d("CheckingSmth", hashMap.toString() + movierItem.title)
                        viewModel.getUserMovies(FirebaseAuth.getInstance().currentUser!!.uid)
                    }
                    .addOnFailureListener {
                        Log.e("CheckingSmth", it.message!!)
                    }
             //   Toast.makeText(context, movierItem.title, Toast.LENGTH_SHORT).show()
            }
            // foodItems[movierItem.id] = movierItem
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    bgColor,
                    RoundedCornerShape(16.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Text(
                    text = if(isAdding.value) "Add to the list" else title,
                    fontSize = 18.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

        }
    }
    //  }
}

@Composable
fun MovieItemCard(
    movieItem: MovierItem,
    isAdding: MutableState<Boolean>,
    modifier: Modifier,
    screenHeight: Float,
    navController: NavController,
    screenWidth: Float
) {
    val context = LocalContext.current
    val image by remember(movieItem){
        mutableStateOf("https://image.tmdb.org/t/p/w500" + movieItem.posterUrl)
    }
    DragTarget(modifier = Modifier, dataToDrop = movieItem, isAdding = isAdding, key = Random.nextInt(1, 100)) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width((screenWidth * 0.40).dp)
                    .height((screenHeight * 0.34).dp)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(16.dp)
                    )
              /*      .pointerInput(Unit){
                        detectTapGestures (
                            onTap = { },
                            onPress = {
                                showToast(context, movieItem.title)
                                Log.d("PosterCh", image)
                            }
                        )
                    }

               */
                    .clickable {
                        navController.navigate(MovierScreens.UpdateScreen.name + "/${movieItem.id}")
                    }
                               ,

                contentDescription = "Movie Image"
            )
        }
}

@Composable
fun MovieItemsRow(
    movieList: List<MovierItem>,
    cardModifier: Modifier = Modifier,
    isAdding: MutableState<Boolean>,
    screenHeight: Float,
    navController : NavController,
    screenWidth: Float
) {
    LazyRow() {
        items(movieList) { item ->
            MovieItemCard(
                movieItem = item,
                isAdding = isAdding,
                modifier = cardModifier,
                screenHeight = screenHeight,
                navController = navController,
                screenWidth = screenWidth)
        }
    }
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    modifier: Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    textStyle : TextStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon : @Composable (() -> Unit)? = {},
    onAction : KeyboardActions = KeyboardActions.Default
){
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
    label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = textStyle,
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    imeAction: ImeAction = ImeAction.Done,
    passwordVisibility: MutableState<Boolean>,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    val visualTransformation = if(passwordVisibility.value) VisualTransformation.None
    else PasswordVisualTransformation()
    InputField(
        valueState = passwordState,
        labelId = labelId,
        isSingleLine = true,
        modifier = modifier,
        enabled = enabled,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        onAction = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)}
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(imageVector = Icons.Default.Password, contentDescription = "Password Icon", tint = Color.LightGray)
    }
}

fun showToast(context: Context, message: String, toastLength: Int = Toast.LENGTH_LONG ){
    Toast.makeText(context, message, toastLength).show()
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = MaterialTheme.colors.background) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon", tint = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun FavoriteEpisodes(favoriteEpisodes: MutableState<List<Episode>>, onLongPress: (Episode) -> Unit) {
    LazyRow(){
        items(favoriteEpisodes.value){ episode->
            Card(
                modifier = Modifier
                    .padding(2.dp)
                    /* .pointerInput(Unit){
                                       detectTapGestures(onLongPress = {
                                           Log.d("TestUpdate", episode.toString())
                                          onLongPress(episode)
                                       })
                    }*/
                    .clickable { onLongPress(episode) }
                ,

                shape = RoundedCornerShape(20.dp)) {
                Text(
                    text = "S:" + episode.season + "E:" + episode.episode,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
}