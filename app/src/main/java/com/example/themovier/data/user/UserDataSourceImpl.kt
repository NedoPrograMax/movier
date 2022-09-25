package com.example.themovier.data.user

import android.net.Uri
import android.util.Log
import com.example.themovier.domain.models.MovierUserModel
import com.example.themovier.domain.user.UserDataSource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
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

    override suspend fun getUsersInfo(list: List<String>): Result<List<MovierUserModel>, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val users =
                    usersCollection.whereIn("userId", list).get().await().map { document ->
                        document.toObject(MovierUserModel::class.java)
                    }
                Ok(users)
            } catch (e: Exception) {
                Err(e)
            }
        }

    override suspend fun createUserItem(email: String, userId: String): Result<Unit, Exception> {
        val name = email.split("@")[0]
        val user = MovierUserModel(
            userId = userId,
            name = name,
            email = email,
            profileUrl = "",
        )
        return try {
            usersCollection
                .add(user)
            Ok(Unit)
        } catch (e: Exception) {
            Err(e)
        }
    }

    override suspend fun updateUserProfileData(
        userHashMap: Map<String, Any>,
        userId: String,
    ): Result<Unit, Exception> =
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


                if (result.isComplete || result.exception == null) Ok(Unit)
                else {
                    Log.e("USERRRR", result.exception.toString())
                    Err(result.exception!!)
                }


            } catch (e: Exception) {
                Log.e("USERRRR", e.toString())
                e.printStackTrace()
                Err(e)
            }
        }

    override suspend fun putImage(uri: Uri): Result<Uri, Exception> = withContext(Dispatchers.IO) {
        val storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference.child("myImages/" + UUID.randomUUID().toString())
        try {
            val puttingFile = ref.putFile(uri).await()
            (if (puttingFile.task.isSuccessful) {
                val result = puttingFile.task.snapshot.metadata!!.reference!!.downloadUrl
                Ok(result.await())
            } else {
                Err(Exception("Task's not successful"))
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Err(e)
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit, Exception> = withContext(Dispatchers.IO) {
        try {
            val task = auth.createUserWithEmailAndPassword(email, password).await()
            task.user?.uid?.let { uid ->
                val itemCreated = createUserItem(email, uid)
                itemCreated
            } ?: Err(Exception("user id is not provided"))
        } catch (e: Exception) {
            e.printStackTrace()
            Err(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit, Exception> = withContext(Dispatchers.IO) {
        try {
            val user = auth.signInWithEmailAndPassword(email, password)
                .await()
            val result = user.user?.uid?.isNotBlank()!!
            if (result) Ok(Unit)
            else Err(Exception("user id is not provided"))
        } catch (e: Exception) {
            e.printStackTrace()
            Err(e)
        }
    }


}