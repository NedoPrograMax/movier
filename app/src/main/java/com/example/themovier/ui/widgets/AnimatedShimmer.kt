package com.example.themovier.ui.widgets

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themovier.ui.models.HomeScreenMenuItem
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.screens.home.DrawerBody
import com.example.themovier.ui.screens.home.DrawerHeader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun AnimatedShimmer(
    screen: MovierScreens = MovierScreens.HomeScreen,
    navController: NavController,
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ))

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    ShimmerItem(brush = brush, screen = screen, navController = navController)
}

@Composable
fun ShimmerItem(
    brush: Brush,
    screen: MovierScreens = MovierScreens.HomeScreen,
    navController: NavController?
) {
    Log.d("RRRR", "Rr")
    if (screen == MovierScreens.HomeScreen) {
        Scaffold(
            topBar = {
                MovierAppBar(
                    title = "Your movie lists",
                    icon = Icons.Default.Menu,
                    onIconClick = {},
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout Icon"
                            )
                        }
                    })
            },
            floatingActionButton = {
                FABContent {
                    navController?.navigate(MovierScreens.SearchScreen.name)
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(it),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    LoadingAddingArea(title = "Watching now")
                    Row() {
                        LazyRow {
                            items((1..5).toList()) {
                                Card(
                                    modifier = Modifier
                                        //  .clip(RoundedCornerShape(20))
                                        .width(160.dp)
                                        .height(230.dp)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Spacer(modifier = Modifier
                                        .fillMaxSize()
                                        .background(brush = brush))
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.weight(1F)) {
                    LoadingAddingArea(title = "Watch later")
                    Row() {
                        LazyRow {
                            items((1..5).toList()) {
                                Card(
                                    modifier = Modifier
                                        //  .clip(RoundedCornerShape(20))
                                        .width(160.dp)
                                        .height(230.dp)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Spacer(modifier = Modifier
                                        .fillMaxSize()
                                        .background(brush = brush))
                                }
                            }
                        }
                    }
                }
            }
        }

    } else if (screen == MovierScreens.UpdateScreen) {
        Scaffold(
            topBar = {
                MovierAppBar(
                    title = "Update",
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { navController?.popBackStack()},
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                )
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it),
                horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(350.dp)
                            .background(brush = brush)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(105.dp)
                            .background(brush = brush)
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(43.dp)
                            .background(brush = brush)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(41.dp)
                            .background(brush = brush)
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(29.dp)
                            .background(brush = brush)
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(29.dp)
                            .background(brush = brush)
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(29.dp)
                            .background(brush = brush)
                    )
                }
            }
        }
    }
}