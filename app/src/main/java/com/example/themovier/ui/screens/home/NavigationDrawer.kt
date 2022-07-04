package com.example.themovier.ui.screens.home

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
import com.example.themovier.data.datasource.FirebaseDataSourceImpl
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun DrawerHeader(
    imageUrl: String,
    userDocId: String,
    enabled: Boolean,
    name: String,
    onImageClick: () -> Unit
){
    var imageUrlMy = imageUrl
    val nameState = remember{
        mutableStateOf(name)
    }
    var imageLoading by remember(enabled){
        mutableStateOf(enabled)
    }
    val firebaseDataSource = FirebaseDataSourceImpl()


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
                .data(imageUrl.ifBlank { ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24) })
                .crossfade(true)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .clickable {
                    onImageClick()
                }
            ,
            contentDescription = "Profile Icon")

    //   Text(text = "Name", fontSize = 24.sp)
       TextField(
           value = nameState.value,
           onValueChange = {nameState.value = it},
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

       Button(
           modifier = Modifier.fillMaxWidth(0.6f),
           enabled = (imageUrlMy.isNotBlank() && enabled) || (nameState.value != name),
           onClick = {
               if(enabled && nameState.value != name) {
                   val storageReference = FirebaseStorage.getInstance().reference
                   val ref = storageReference.child("myImages/" + UUID.randomUUID().toString())
                   ref.putFile(imageUrlMy.toUri()).addOnSuccessListener { taskSnapshot ->
                       if (taskSnapshot.task.isSuccessful) {
                           taskSnapshot.task.snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { result ->
                               imageUrlMy = result.toString()
                               firebaseDataSource.updateUserProfileData(
                                   hashMapOf(
                                       "profileUrl" to imageUrlMy,
                                       "name" to nameState.value,
                                   ) as Map<String, Any>,
                                   userDocId
                               )
                               Log.i("ImageTesti", imageUrlMy)
                           }

                       }
                   }
                       .addOnFailureListener {
                           Log.e("ImageTest", it.message!!)
                       }
               }
               else if(enabled) {
                   val storageReference = FirebaseStorage.getInstance().reference
                   val ref = storageReference.child("myImages/" + UUID.randomUUID().toString())
                   ref.putFile(imageUrlMy.toUri()).addOnSuccessListener { taskSnapshot ->
                       if (taskSnapshot.task.isSuccessful) {
                           taskSnapshot.task.snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { result ->
                               imageUrlMy = result.toString()
                               firebaseDataSource.updateUserProfileData(
                                   hashMapOf(
                                       "profileUrl" to imageUrlMy
                                   ) as Map<String, Any>,
                                   userDocId
                               )
                               Log.i("ImageTesti", imageUrlMy)
                           }

                       }
                   }
                       .addOnFailureListener {
                           Log.e("ImageTest", it.message!!)
                       }
               }

               else if(nameState.value != name){
                   firebaseDataSource.updateUserProfileData(
                       hashMapOf(
                           "name" to imageUrlMy
                       ) as Map<String, Any>,
                       userDocId
                   )
               }
               }
       ) {
                Text(text = "Update")
       }
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
){
    LazyColumn(modifier){
        items(items){item->
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
