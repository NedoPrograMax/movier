package com.example.themovier.data.user

import com.example.themovier.data.models.MovierUserModel
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface UserDataSource {
    suspend fun getUser(userId: String): Result<MovierUserModel, Exception>
    suspend fun createUser(email: String, userId: String): Result<Unit, Exception>
    suspend fun updateUser(userId: String): Result<Unit, Exception>
}

class UserDataSourceImpl : UserDataSource {
    private val userCollection = FirebaseFirestore.getInstance().collection("users")

    override suspend fun getUser(userId: String) = withContext(Dispatchers.IO) {
        try {
            val user = userCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .map { document -> document.toObject(MovierUserModel::class.java) }
                .first()

            Ok(user)
        } catch(e: Exception) {
            Err(e)
        }
    }

    override suspend fun createUser(email: String, userId: String) = withContext(Dispatchers.IO) {
        try {
            val name = email.split("@")[0]
            val user = MovierUserModel(
                userId = userId,
                name = name,
                email = email,
                userPicture = "",
            )

            userCollection.add(user)
            Ok(Unit)
        } catch(e: Exception) {
            Err(e)
        }
    }

    override suspend fun updateUser(userId: String) = withContext(Dispatchers.IO) {
        userCollection.whereEqualTo("userId", userId).get().await().documents.first().reference.update(mapOf("a" to 1))
        Ok(Unit)
    }
}
