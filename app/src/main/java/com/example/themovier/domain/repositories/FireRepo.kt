package com.example.themovier.domain.repositories

import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.models.MovierUser

interface FireRepo {
    suspend fun getUserInfo(userId: String): Result<MovierUser>
    suspend fun getUserMovies(userId: String): Result<List<MovierItem>>
    suspend fun getMovie(movieId: String): Result<MovierItem>
}