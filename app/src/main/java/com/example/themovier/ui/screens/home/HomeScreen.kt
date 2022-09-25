package com.example.themovier.ui.screens.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController,
    selectImageLauncher: ActivityResultLauncher<Intent>,
    imageState: MutableState<Uri?>,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()

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
    val userData = state.dataUser
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
                        imageState.value = null
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
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center
            ) {
                if (state.loading && state.dataMovies.isEmpty()) {
                    LinearProgressIndicator()
                    //   AnimatedShimmer()
                } else {
                    HomeContent(navController = navController, viewModel)
                }
            }
        }
    }


    if (state.loading) {
        AnimatedShimmer(navController = navController)
    }
}


@Composable
fun HomeContent(
    navController: NavController,
    viewModel: HomeScreenViewModel,
) {
    val state by viewModel.state.collectAsState()

    val isAddingWatched = remember {
        mutableStateOf(false)
    }
    val isAddingUnWatched = remember {
        mutableStateOf(false)
    }

    val watchedMovieList = state.dataMovies.filter { movie ->
        movie.startDate.isNotBlank() && movie.finishDate.isBlank()
    }
    val unwatchedMovieList = state.dataMovies.filter { movie ->
        movie.startDate.isBlank()
    }

    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                modifier = Modifier.weight(1F)
            ) {
                AddingArea(isAddingWatched, viewModel, "Watching now")

                MovieItemsRow(
                    movieList = watchedMovieList,
                    isAdding = isAddingUnWatched,
                    navController = navController,
                )

            }

            Column(modifier = Modifier.weight(1F)) {
                AddingArea(isAddingUnWatched, viewModel, "Watch later")

                MovieItemsRow(
                    movieList = unwatchedMovieList,
                    isAdding = isAddingWatched,
                    navController = navController,
                )

            }
        }

    }
}






