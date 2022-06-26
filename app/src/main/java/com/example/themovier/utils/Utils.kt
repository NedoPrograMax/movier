package com.example.themovier.utils

import android.provider.MediaStore
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean{
    return !status.shouldShowRationale && !status.isGranted
}

