package com.example.themovier.domain.user

import com.example.themovier.domain.models.MovierUserModel

interface UserDataSource {
    suspend fun getUserInfo(userId: String): com.github.michaelbull.result.Result<MovierUserModel, Throwable>
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        onSuccess: (String) -> Unit = {},
        home: () -> Unit,
    )

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