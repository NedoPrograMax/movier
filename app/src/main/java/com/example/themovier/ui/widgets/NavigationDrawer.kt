package com.example.themovier.ui.screens.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovier.R
import com.example.themovier.ui.models.HomeScreenMenuItem
import com.example.themovier.ui.widgets.LoadingDialog
import com.example.themovier.ui.widgets.showToast
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch


@Composable
fun DrawerHeader(
    viewModel: HomeScreenViewModel,
    name: String,
    imageUrl: String,
    enabled: Boolean,
    onUpdate: () -> Unit,
    onImageClick: () -> Unit,
) {
    var imageUrlMy = imageUrl
    val nameState = remember {
        mutableStateOf(name)
    }
    var imageLoading by remember(enabled) {
        mutableStateOf(enabled)
    }
    var loadingCircle by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl.ifBlank {
                    ContextCompat.getDrawable(context,
                        R.drawable.ic_baseline_account_circle_24)
                })
                .crossfade(true)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .clickable {
                    onImageClick()
                },
            contentDescription = "Profile Icon")

        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            modifier = Modifier.fillMaxWidth(0.6f),
            textStyle = TextStyle(
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        var userUpdatingState by remember {
            mutableStateOf<Result<Uri, Exception>?>(null)
        }

        var exceptionUpdatingState by remember {
            mutableStateOf<Exception?>(null)
        }

        LaunchedEffect(Unit) {
            launch {
                viewModel.uriUpdateSharedFlow
                    .collect { result ->
                        userUpdatingState = result
                    }
            }
            launch {
                viewModel.exceptionUpdateSharedFlow
                    .collect { result ->
                        exceptionUpdatingState = result
                    }
            }
        }

        if (loadingCircle) {
            LoadingDialog()
            if (enabled && nameState.value != name) {
                userUpdatingState?.onSuccess { uri ->
                    viewModel.updateUserProfileData(
                        hashMapOf(
                            "profileUrl" to uri.toString(),
                            "name" to nameState.value,
                        ) as Map<String, Any>,
                    )
                    //  loadingCircle = false
                    //    onUpdate()
                }

            } else if (enabled) {
                userUpdatingState?.onSuccess { uri ->
                    viewModel.updateUserProfileData(
                        hashMapOf(
                            "profileUrl" to uri.toString(),
                        ) as Map<String, Any>,
                    )
                    //    loadingCircle = false
                    // onUpdate()
                }
            } else if (nameState.value != name) {
                viewModel.updateUserProfileData(
                    hashMapOf(
                        "name" to nameState.value
                    ) as Map<String, Any>,
                )
                //   loadingCircle = false
                // onUpdate()
            }
            exceptionUpdatingState?.let {
                if (it.message.isNullOrBlank()) {
                    onUpdate()
                } else {
                    showToast(context, it.message!!)
                }
                loadingCircle = false
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            enabled = (imageUrlMy.isNotBlank() && enabled) || (nameState.value != name),
            onClick = {
                loadingCircle = true
                if (enabled && nameState.value != name) {
                    imageUrlMy = viewModel.putImage(imageUrlMy.toUri()).toString()


                } else if (enabled) {
                    imageUrlMy = viewModel.putImage(imageUrlMy.toUri()).toString()
                }
            }
        ) {
            Text(text = "Update")
        }
    }
}

@Composable
fun DrawerBody(
    items: List<HomeScreenMenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (HomeScreenMenuItem) -> Unit,
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
