package com.example.themovier.data.utils

import android.icu.text.DateFormat
import android.util.Log
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !status.shouldShowRationale && !status.isGranted
}

fun formatDate(timestamp: Timestamp): String {

    return DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")[0]
}

fun List<String>.onLikeClick(): List<String> = this.run {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val userInList = find { it == currentUserId }
    if (userInList != null) {
        minus(userInList)
    } else {
        plus(currentUserId!!)
    }
}

fun getNumberOfLikes(hadUserLiked: Boolean, isLiked: Boolean, initialNumber: Int): Int {
   // Log.d("ERRyy", hadUserLiked.toString() + " " + isLiked.toString() + initialNumber.toString())

    return initialNumber
}





