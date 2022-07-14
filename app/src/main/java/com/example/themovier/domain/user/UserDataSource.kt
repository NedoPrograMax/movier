package com.example.themovier.domain.user

import com.example.themovier.domain.models.MovierUserModel
import com.github.michaelbull.result.Result
import java.util.*

interface UserDataSource {
    suspend fun getUserInfo(userId: String): Result<MovierUserModel, Exception>
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit,
    ): Result<UUID, Exception>

    fun createUserItem(email: String, userId: String)
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        home: () -> Unit,
    )

    suspend fun updateUserProfileData(
        userHashMap: Map<String, Any>,
        userId: String,
        onSuccess: () -> Unit,
    )
}