package com.example.themovier.widgets

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.themovier.model.MovieItem
import com.example.themovier.utils.DragTarget
import com.example.themovier.utils.DropTarget

@Composable
fun AddingArea(isAdding: MutableState<Boolean>) {

    val context = LocalContext.current

    DropTarget<MovieItem>(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) { isInBound, foodItem ->
        val bgColor = if (!isAdding.value)
            Color.Transparent
        else  if (isInBound) {
            Color.Red
        } else {
            Color.White
        }

        foodItem?.let {
            if (isInBound) {
                Toast.makeText(context, foodItem.title, Toast.LENGTH_SHORT).show()
            }
            // foodItems[foodItem.id] = foodItem
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

            if(isAdding.value) {
                Text(
                    text = "Area",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
    //  }
}

@Composable
fun MovieItemCard(
    movieItem: MovieItem,
    isAdding: MutableState<Boolean>,
    modifier: Modifier,
    screenHeight: Float,
    screenWidth: Float
) {

    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
        ) {
            DragTarget(modifier = Modifier, dataToDrop = movieItem, isAdding) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width((screenWidth * 0.45).dp)
                        .height((screenHeight * 0.18).dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Text(
                text = movieItem.title,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MovieItemsRow(
    movieList: List<MovieItem>,
    cardModifier: Modifier = Modifier,
    isAdding: MutableState<Boolean>,
    screenHeight: Float,
    screenWidth: Float
) {
    LazyRow() {
        items(movieList) { item ->
            MovieItemCard(
                movieItem = item,
                isAdding = isAdding,
                modifier = cardModifier,
                screenHeight = screenHeight,
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon : @Composable (() -> Unit)? = {},
    onAction : KeyboardActions = KeyboardActions.Default
){
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
    label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
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