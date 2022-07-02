package com.example.themovier.data.firebase

import android.util.Log
import com.example.themovier.domain.FirebaseDataSource
import com.example.themovier.domain.FirebaseUser
import com.example.themovier.model.MovierUser
import com.google.firebase.firestore.FirebaseFirestore

class UserModel(val a: String)

class FirebaseDataSourceImpl : FirebaseDataSource {
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun createUser(email: String, userId: String) {
        val name = email.split("@")[0]
        val user = MovierUser(
            userId = userId,
            name = name,
            email = email,
            profileUrl = ""
        )
        firebaseFirestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentRef ->
                val documentId = documentRef.id
                firebaseFirestore.collection("users")
                    .document(documentId)
                    .update(hashMapOf("id" to documentId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("UpdatingUser", "Good")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("UpdatingUser", it.message!!)
                    }
            }
    }

    override fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String) {
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

    override fun getUser(): FirebaseUser {

        return FirebaseUser("asdfas")
    }
}

