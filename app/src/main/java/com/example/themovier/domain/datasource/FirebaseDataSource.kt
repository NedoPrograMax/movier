package com.example.themovier.domain.datasource

import com.example.themovier.data.models.MovierItem

interface FirebaseDataSource {
    fun createUser(email: String, userId: String)
    fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String)
    fun createMovie(movie: MovierItem)
    fun deleteMovie(movieId: String)

}