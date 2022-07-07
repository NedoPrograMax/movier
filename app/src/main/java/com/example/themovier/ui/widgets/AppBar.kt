package com.example.themovier.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@Composable
fun MovierAppBar(
    title: String,
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    IconButton(onClick = onIconClick) {
                        Icon(imageVector = icon, contentDescription = "App Bar Icon")
                    }
                }
                Text(text = title, fontSize = 16.sp)
            }
        },
        actions = {
            actions()
        }
    )
}