package com.example.themovier.data.user

import android.util.Log
import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor() : UserDataSource {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firebaseFirestore.collection("users")
    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun getUserInfo(userId: String): Result<MovierUserModel, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val user =
                    usersCollection.whereEqualTo("userId", userId).get().await().map { document ->
                        document.toObject(MovierUserModel::class.java)
                    }.first()
                Ok(user)
            } catch (e: Exception) {
                Err(e)
            }
        }

    override suspend fun createUserItem(email: String, userId: String): Exception {
        val name = email.split("@")[0]
        val user = MovierUserModel(
            userId = userId,
            name = name,
            email = email,
            profileUrl = "",
        )
        return try {
            usersCollection.add(user)
            Exception("")
        } catch (e: Exception) {
            e
        }
    }

    override suspend fun updateUserProfileData(
        userHashMap: Map<String, Any>,
        userId: String,
    ): Exception = // rewrite to Result class
        withContext(Dispatchers.IO) {
            try {
                val result = usersCollection
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
                    .documents
                    .first()
                    .reference
                    .update(userHashMap)

                if (result.isSuccessful) {
                    Exception("")
                } else {
                    result.exception!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
                e
            }
        }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): Exception = withContext(Dispatchers.IO) {
        try {
            val task = auth.createUserWithEmailAndPassword(email, password).await()
            task.user?.uid?.let { uid ->
                val itemCreated = createUserItem(email, uid)
                val result = if (itemCreated.message.isNullOrBlank()) {
                    Exception("")
                } else {
                    itemCreated
                }

                result
            } ?: Exception("user id is not provided")
        } catch (e: Exception) {
            e.printStackTrace()
            e
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Exception = withContext(Dispatchers.IO) {
        try {
            val user = auth.signInWithEmailAndPassword(email, password)
                .await()
            val result = user.user?.uid?.isNotBlank()!!
            if (result) Exception("")
            else Exception("user id is not provided")
        } catch (e: Exception) {
            e.printStackTrace()
            e
        }
    }


}