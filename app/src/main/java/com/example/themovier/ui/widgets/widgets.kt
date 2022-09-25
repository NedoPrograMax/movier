package com.example.themovier.ui.widgets

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.themovier.data.models.Comment
import com.example.themovier.data.models.Episode
import com.example.themovier.data.utils.formatDate
import com.example.themovier.data.utils.getNumberOfLikes
import com.example.themovier.data.utils.onLikeClick
import com.example.themovier.ui.models.DetailsUIModel
import com.example.themovier.ui.models.FullComment
import com.example.themovier.ui.models.HomeUIModel
import com.example.themovier.ui.models.UpdateUiModel
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.home.HomeIntent
import com.example.themovier.ui.screens.home.HomeScreenViewModel
import com.example.themovier.ui.screens.update.UpdateState
import com.example.themovier.ui.screens.update.UpdateViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

@Composable
fun AddingArea(isAdding: MutableState<Boolean>, viewModel: HomeScreenViewModel, title: String) {

    val context = LocalContext.current

    DropTarget<HomeUIModel>(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) { isInBound, homeItem ->
        val bgColor = if (!isAdding.value)
            Color.Transparent
        else if (isInBound) {
            Color.Red
        } else {
            Color.White
        }

        homeItem?.let {
            val date = formatDate(Timestamp.now())
            if (isInBound) {

                val hashMap =
                    if (homeItem.startDate.isBlank()) hashMapOf<String, Any>("startDate" to date)
                    else hashMapOf<String, Any>("startDate" to "")
                FirebaseFirestore.getInstance().collection("movies")
                    .document(homeItem.id)
                    .update(hashMap)
                    .addOnSuccessListener {
                        viewModel.processIntent(HomeIntent.GetUserMovies)
                    }
                    .addOnFailureListener {
                        Log.e("CheckingSmth", it.message!!)
                    }
            }
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
                text = if (isAdding.value) "Add to the list" else title,
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }
    }

}

@Composable
fun LoadingAddingArea(title: String) {
    Surface(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) {
        val bgColor =
            Color.Transparent

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
                text = title,
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
fun MovieItemCard(
    movieItem: HomeUIModel,
    isAdding: MutableState<Boolean>? = null,
    navController: NavController,
) {
    val context = LocalContext.current
    val image by remember(movieItem) {
        mutableStateOf("https://image.tmdb.org/t/p/w500" + movieItem.posterUrl)
    }
    if (isAdding != null) {
        DragTarget(
            modifier = Modifier,
            dataToDrop = movieItem,
            isAdding = isAdding,
            key = Random.nextInt(1, 100)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(160.dp)
                    .height(230.dp)
                    .padding(2.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        navController.navigate(MovierScreens.UpdateScreen.name + "/${movieItem.id}")
                    },

                contentDescription = "Movie Image"
            )
        }
    } else {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(160.dp)
                .height(230.dp)
                .padding(2.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    navController.navigate(MovierScreens.UpdateScreen.name + "/${movieItem.id}")
                },

            contentDescription = "Movie Image"
        )
    }
}

@Composable
fun MovieItemsRow(
    movieList: List<HomeUIModel>,
    isAdding: MutableState<Boolean>? = null,
    navController: NavController,
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(movieList) { item ->
            MovieItemCard(
                movieItem = item,
                isAdding = isAdding,
                navController = navController,
            )
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
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = {},
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
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
fun InputFieldNote(
    value: String?,
    onValueChange: (String) -> Unit,
    labelId: String,
    isSingleLine: Boolean = true,
    modifier: Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = {},
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value ?: "",
        onValueChange = onValueChange,
        label = { Text(text = labelId) },
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
    onAction: KeyboardActions = KeyboardActions.Default,
) {
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
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None
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
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) }
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icon(imageVector = Icons.Default.Password,
            contentDescription = "Password Icon",
            tint = Color.LightGray)
    }
}

fun showToast(context: Context, message: String, toastLength: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, toastLength).show()
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = MaterialTheme.colors.background) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Add Icon",
            tint = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun FavoriteEpisodes(
    favoriteEpisodes: MutableState<List<Episode>>,
    onLongPress: (Episode) -> Unit,
) {
    LazyRow {
        items(favoriteEpisodes.value) { episode ->
            Card(
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { onLongPress(episode) },

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

@Composable
fun DetailsUIModel.MovieDescription(
    listComment: Map<String, Comment>? = null,
    viewModel: UpdateViewModel? = null,
    onUpdate: (Map<String, Comment>) -> Unit = {},

    ) {

    Column(
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Movie Info",
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
        )

        DropDown(title = "Description") {
            Text(
                text = description,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 1.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
            )
        }

        DescriptionRow(title = "Movie Title", text = title)

        val genress = genres.map {
            it.name
        }.toString()

        DescriptionRow(title = "Genres", text = genress)

        DescriptionRow(title = "Status", text = status)

        DescriptionRow(title = "Language", text = language)

        DescriptionRow(title = "Release Date", text = releaseDate)

        if (listComment != null) {
            CommentsColumn(list = comments, listComment, onUpdate, viewModel!!)
        }

    }
}

@Composable
fun DescriptionRow(title: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 1.dp)
                .weight(1F),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 1.dp)
                .weight(1F),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.weight(1F))
    }
}

@Composable
fun CommentsColumn(
    list: Map<String, FullComment>,
    listComment: Map<String, Comment>,
    onUpdate: (Map<String, Comment>) -> Unit,
    viewModel: UpdateViewModel,
) {
    Text(
        text = "Comments",
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 1.dp)
            .fillMaxWidth(),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )


    Column {
        CommentItem(list, listComment, onUpdate, viewModel)
    }
}

@Composable
private fun CommentItem(
    list: Map<String, FullComment>,
    listComment: Map<String, Comment>,
    onUpdate: (Map<String, Comment>) -> Unit,
    viewModel: UpdateViewModel,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    list.forEach { (id, item) ->
        val hadUserLiked =
            state.listComment!![id]!!.likeUsersIdList.contains(FirebaseAuth.getInstance().currentUser?.uid)

        var isLiked by remember {
            mutableStateOf(hadUserLiked)
        }

        Card(
            modifier = Modifier.padding(5.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            elevation = 6.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = CenterVertically
                ) {

                    Column(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(item.userPicture)
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(
                                    CircleShape
                                )
                                .size(32.dp),
                            contentDescription = "User Image"
                        )

                        Text(
                            text = item.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )

                    }

                    Text(
                        text = item.comment.text,
                        fontSize = 12.sp,
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        val newItem =
                            state.listComment!![id]!!.copy(likeUsersIdList = state.listComment!![id]!!.likeUsersIdList.onLikeClick())
                        val newList =  state.listComment!!.minus(id).plus(id to newItem)

                        Log.d("Weee", newList.toString())
                        onUpdate(newList)
                        isLiked = !isLiked
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.LightGray,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    Text(
                        text =
                        getNumberOfLikes(hadUserLiked,
                            isLiked,
                            state.listComment!![id]!!.likeUsersIdList.size).toString(),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

fun MutableList<String>.color() = this.run {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    if (contains(currentUserId)) Color.Red
    else Color.LightGray
}

@Composable
fun isUpdateButtonEnabled(
    state: UpdateState,
    movie: UpdateUiModel,
    someButtonClicked: Boolean,
    season: Int,
    episode: Int,
    favoriteEpisodes: MutableState<List<Episode>>,
    resourceState: MutableState<String>,
): Boolean {
    return (state.note != movie.note || someButtonClicked || movie.season != season || movie.episode != episode
            || movie.favoriteEpisodes != favoriteEpisodes.value.toList()
            || resourceState.value != movie.resource || state.listComment != state.data?.comments?.mapValues { it.value.comment })
}

@Composable
fun DropDown(
    title: String,
    modifier: Modifier = Modifier,
    initiallyOpened: Boolean = false,
    content: @Composable () -> Unit,
) {
    var isOpen by remember {
        mutableStateOf(initiallyOpened)
    }
    val alpha = animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400
        )
    )

    val rotateX = animateFloatAsState(
        targetValue = if (isOpen) 0f else -90f,
        animationSpec = tween(
            durationMillis = 400
        )
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                // color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp)
            IconButton(onClick = { isOpen = !isOpen }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open/Close",
                    modifier = Modifier.scale(1f, if (isOpen) -1f else 1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    rotationX = rotateX.value
                }
                .alpha(alpha.value)
        ) {
            if(rotateX.value != -90f) content()
        }
    }
}

