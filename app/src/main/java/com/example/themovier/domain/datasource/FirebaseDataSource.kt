package com.example.themovier.domain.datasource

import com.example.themovier.domain.models.MovierItemModel

interface FirebaseDataSource {
    fun createUser(email: String, userId: String)
    fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String)
    fun createMovie(movie: MovierItemModel)
    fun deleteMovie(movieId: String)

}
