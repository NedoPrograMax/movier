package com.example.themovier.domain.repositories

import com.example.themovier.data.models.MovierUserModel
import com.example.themovier.domain.models.MovierItemModel

interface FireRepo {
    suspend fun getUserInfo(userId: String): com.github.michaelbull.result.Result<MovierUserModel, Throwable>
    suspend fun getUserMovies(userId: String): com.github.michaelbull.result.Result<List<MovierItemModel>, Throwable>
    suspend fun getMovie(movieId: String): com.github.michaelbull.result.Result<MovierItemModel, Throwable>
}
