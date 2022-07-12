package com.example.themovier.ui.screens.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovier.data.utils.isPermanentlyDenied
import com.example.themovier.ui.models.HomeScreenMenuItem
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.widgets.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    selectImageLauncher: ActivityResultLauncher<Intent>,
    imageState: MutableState<Uri?>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    permissionState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when {
                    perm.status.isGranted -> {
                        //  showToast(context, "Read external storage permission's granted")
                    }

                    perm.status.shouldShowRationale -> {
                        showToast(
                            context,
                            "Read external storage permission's needed to set your profile image"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        showToast(
                            context,
                            "Read external storage permission was permanently denied, " +
                                    "you can enable it in app settings"
                        )
                    }
                }
            }
        }
    }
    val userData = viewModel.dataUser.value
    userData?.let {
        Log.d("TestingUser", userData.name)
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MovierAppBar(
                    title = "Your movie lists",
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            Firebase.auth.signOut()
                            navController.navigate(MovierScreens.LoginScreen.name) {
                                popUpTo(MovierScreens.HomeScreen.name) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout Icon"
                            )
                        }
                    })
            },
            drawerContent = {
                DrawerHeader(
                    imageUrl = if (imageState.value == null) userData.profileUrl else {
                        imageState.value.toString()
                    },
                    name = userData.name,
                    viewModel = viewModel,
                    enabled = imageState.value != null,
                    onUpdate = {
                        //    viewModel.getUserData()
                        navController.navigate(MovierScreens.HomeScreen.name) {
                            popUpTo(MovierScreens.HomeScreen.name) {
                                inclusive = true
                            }
                        }


                    },
                ) {
                    permissionState.launchMultiplePermissionRequest()
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    selectImageLauncher.launch(pickIntent)
                }
                DrawerBody(
                    items = listOf(
                        HomeScreenMenuItem(
                            id = MovierScreens.AboutScreen.name,
                            title = "About",
                            icon = Icons.Default.Info
                        ),
                        HomeScreenMenuItem(
                            id = MovierScreens.StatsScreen.name,
                            title = "Stats",
                            icon = Icons.Default.WorkHistory
                        )
                    ),
                    onItemClick = {
                        navController.navigate(it.id)
                    })
            },
            floatingActionButton = {
                FABContent {
                    navController.navigate(MovierScreens.SearchScreen.name)
                }
            }
        ) {
            it
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if (viewModel.loadingMovies.value && viewModel.dataMovies.value.isNullOrEmpty()) {
                    LinearProgressIndicator()
                } else {
                    HomeContent(navController = navController, viewModel)
                }
            }
        }
    }


    if (viewModel.loadingUser.value) {
        CircularProgressIndicator()
    }
}


@Composable
fun HomeContent(
    navController: NavController,
    viewModel: HomeScreenViewModel,
) {

    val isAddingWatched = remember {
        mutableStateOf(false)
    }
    val isAddingUnWatched = remember {
        mutableStateOf(false)
    }

    val watchedMovieList = viewModel.dataMovies.value?.filter { movie ->
        movie.startDate.isNotBlank() && movie.finishDate.isBlank()
    }!!
    val unwatchedMovieList = viewModel.dataMovies.value?.filter { movie ->
        movie.startDate.isBlank()
    }!!
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels / displayMetrics.density
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val cardModifier = Modifier
        .width((screenWidth * 0.45).dp)
        .height((screenHeight * 0.3).dp)

    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        Column {
            AddingArea(isAddingWatched, viewModel, "Watching now")

            Log.d("WathedListM", watchedMovieList.toString())

            MovieItemsRow(
                movieList = watchedMovieList,
                isAdding = isAddingUnWatched,
                cardModifier = cardModifier,
                screenHeight = screenHeight,
                navController = navController,
                screenWidth = screenWidth
            )

            AddingArea(isAddingUnWatched, viewModel, "Watch later")

            MovieItemsRow(
                movieList = unwatchedMovieList,
                cardModifier = cardModifier,
                isAdding = isAddingWatched,
                screenHeight = screenHeight,
                navController = navController,
                screenWidth = screenWidth
            )

        }

    }
}




