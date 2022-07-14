package com.example.themovier.data.user

import android.util.Log
import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
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

    override fun createUserItem(email: String, userId: String) {
        val name = email.split("@")[0]
        val user = MovierUserModel(
            userId = userId,
            name = name,
            email = email,
            profileUrl = "")
        usersCollection
            .add(user)
            .addOnFailureListener {
                Log.e("Creating User", it.message!!)
            }
    }

    override suspend fun updateUserProfileData(
        userHashMap: Map<String, Any>,
        userId: String,
        onSuccess: () -> Unit
    ): Unit =
        withContext(Dispatchers.IO) {
            usersCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents
                .first()
                .reference
                .update(userHashMap)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("UpdatingUser", e.message!!)
                }
        }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val task = auth.createUserWithEmailAndPassword(email, password).await()
            task.user?.uid?.let { Ok(it) } ?: Err(Exception("user id is not provided"))
        } catch (e: Exception) {
            e.printStackTrace()
            Err(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit,
        home: () -> Unit,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        home()
                    } else {
                        task.exception?.message?.let { onFailure(it) }
                    }
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> onFailure(it1) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}