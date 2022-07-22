package com.example.themovier.data.movie

import android.util.Log
import com.example.themovier.domain.models.MovierItemModel
import com.example.themovier.domain.movie.MovieDataSource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDataSourceImpl @Inject constructor() : MovieDataSource {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val moviesCollection = firebaseFirestore.collection("movies")


    override suspend fun getUserMovies(userId: String): Result<List<MovierItemModel>, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val movies = moviesCollection
                    .whereEqualTo("userId", userId).get().await().map { document ->
                        document.toObject(MovierItemModel::class.java)
                    }
                Ok(movies)
            } catch (e: Exception) {
                Err(e)
            }
        }

    override suspend fun getMovie(movieId: String): Result<MovierItemModel, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val movie = moviesCollection
                    .whereEqualTo("id", movieId).get().await().map { document ->
                        document.toObject(MovierItemModel::class.java)
                    }.first()
                Ok(movie)
            } catch (e: Exception) {
                Err(e)
            }
        }


    override suspend fun createMovie(movie: MovierItemModel) {
        moviesCollection
            .add(movie)
            .addOnSuccessListener { documentRef ->
                val documentId = documentRef.id
                firebaseFirestore.collection("movies")
                    .document(documentId)
                    .update(hashMapOf("id" to documentId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
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

    override suspend fun deleteMovie(movieId: String) {
        moviesCollection
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