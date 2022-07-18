package com.example.themovier.data.utils

import android.icu.text.DateFormat
import android.net.Uri
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*


@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !status.shouldShowRationale && !status.isGranted
}

fun formatDate(timestamp: Timestamp): String {

    return DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")[0]
}

