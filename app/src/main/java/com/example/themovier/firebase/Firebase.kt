package com.example.themovier.firebase

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.example.themovier.model.MovierUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap


fun createUser(email: String, userId: String) {
    val name = email.split("@")[0]
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val user = MovierUser(
        userId = userId,
        name = name,
        email = email,
        profileUrl = "")
    firebaseFirestore.collection("users")
        .add(user)
        .addOnSuccessListener { documentRef->
            val documentId = documentRef.id
            firebaseFirestore.collection("users")
                .document(documentId)
                .update(hashMapOf("id" to documentId) as Map<String, Any>)
                .addOnCompleteListener{task->
                    if (task.isSuccessful){
                        Log.d("UpdatingUser", "Good")
                    }
                }
                .addOnFailureListener {
                    Log.e("UpdatingUser", it.message!!)
                }
        }

}

fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String) {
    FirebaseFirestore.getInstance().collection("users") // Collection Name
        .document(documentId) // Document ID
        .update(userHashMap) // A hashmap of fields which are to be updated.
        .addOnSuccessListener {
            Log.d("UpdatingUser", "Good")
        }
        .addOnFailureListener { e ->
            Log.e("UpdatingUser", e.message!!)
        }
}

