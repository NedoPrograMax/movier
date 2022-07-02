package com.example.themovier.domain

interface FirebaseDataSource {
    fun createUser(email: String, userId: String)
    fun updateUserProfileData(userHashMap: Map<String, Any>, documentId: String)
    fun getUser() : FirebaseUser
}
