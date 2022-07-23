package com.example.themovier.domain.user

import android.net.Uri
import com.example.themovier.domain.models.MovierUserModel
import com.github.michaelbull.result.Result
import java.util.*

interface UserDataSource {
    suspend fun getUserInfo(userId: String): Result<MovierUserModel, Throwable>
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit, Exception>

    suspend fun createUserItem(email: String, userId: String): Result<Unit, Exception>
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit, Exception>

    suspend fun updateUserProfileData(
        userHashMap: Map<String, Any>,
        userId: String,
    ): Result<Unit, Exception>

    suspend fun putImage(uri: Uri): Result<Uri, Exception>
}