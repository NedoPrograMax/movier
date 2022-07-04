package com.example.themovier.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ChooseDialog(
    setShowDialog: (Boolean) -> Unit,
    listOfStrings : List<String>,
    onItemClick: (String) -> Unit
)
{
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxHeight(0.8f)
        ) {
            Box(contentAlignment = Alignment.Center){
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(listOfStrings){item->
                        TextButton(onClick = { onItemClick(item) }) {
                            Text(text = item)
                        }
                    }
                }
            }
        }
    }
}