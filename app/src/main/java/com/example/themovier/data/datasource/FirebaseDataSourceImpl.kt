package com.example.themovier.data.datasource

import android.util.Log
import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.models.MovierUser
import com.example.themovier.domain.datasource.FirebaseDataSource
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDataSourceImpl() : FirebaseDataSource{
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun createUser(email: String, userId: String) {
        val name = email.split("@")[0]
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

    override fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String) {
        firebaseFirestore.collection("users") // Collection Name
            .document(documentId) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                Log.d("UpdatingUser", "Good")
            }
            .addOnFailureListener { e ->
                Log.e("UpdatingUser", e.message!!)
            }
    }

    override fun createMovie(movie: MovierItem){
        firebaseFirestore.collection("movies")
            .add(movie)
            .addOnSuccessListener {   documentRef->
                val documentId = documentRef.id
                firebaseFirestore.collection("movies")
                    .document(documentId)
                    .update(hashMapOf("id" to documentId) as Map<String, Any>)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful){
                            Log.d("UpdatingMovie", "Success")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("UpdatingMovie", it.message!!)
                    }
            }
            .addOnFailureListener {
                Log.e("Create Movie", it.message!!)
            }
    }

    override fun deleteMovie(movieId: String){
        firebaseFirestore.collection("movies")
            .document(movieId)
            .delete()
            .addOnSuccessListener {
                Log.d("DeleteMovie", "Success")
            }
            .addOnFailureListener {
                Log.e("DeleteMovie", it.message!!)
            }
    }
}
