package com.example.themovier.ui.screens.home

import android.net.Uri

sealed class HomeIntent {
    object GetUserData : HomeIntent()
    object GetUserMovies : HomeIntent()
    data class UpdateUserProfileData(
        val map: Map<String, Any>,
    ) : HomeIntent()

    data class PutImage(val uri: Uri) : HomeIntent()
}