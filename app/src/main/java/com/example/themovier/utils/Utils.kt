package com.example.themovier.utils

import android.icu.text.DateFormat
import android.provider.MediaStore
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.Timestamp


@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean{
    return !status.shouldShowRationale && !status.isGranted
}

fun formatDate(timestamp: Timestamp): String {

    return DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")[0]
}