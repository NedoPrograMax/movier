package com.example.themovier.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.themovier.R
import com.example.themovier.ui.navigation.MovierScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            ))
        delay(1500L)

        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrBlank()) {
            navController.navigate(MovierScreens.LoginScreen.name) {
                popUpTo(MovierScreens.SplashScreen.name) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(MovierScreens.HomeScreen.name) {
                popUpTo(MovierScreens.SplashScreen.name) {
                    inclusive = true
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .scale(scale.value)) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontStyle = FontStyle.Italic,
            fontSize = 60.sp)
    }
}
