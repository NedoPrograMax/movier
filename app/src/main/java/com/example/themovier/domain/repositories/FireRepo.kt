package com.example.themovier.domain.repositories

import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.models.MovierUser

interface FireRepo {
    suspend fun getUserInfo(userId: String): com.github.michaelbull.result.Result<MovierUser, Throwable>
    suspend fun getUserMovies(userId: String): com.github.michaelbull.result.Result<List<MovierItem>, Throwable>
    suspend fun getMovie(movieId: String): com.github.michaelbull.result.Result<MovierItem, Throwable>
}
