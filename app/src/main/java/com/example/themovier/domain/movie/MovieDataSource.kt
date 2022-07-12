package com.example.themovier.domain.movie

import com.example.themovier.domain.models.MovierItemModel

interface MovieDataSource {
    suspend fun getUserMovies(userId: String): com.github.michaelbull.result.Result<List<MovierItemModel>, Throwable>
    suspend fun getMovie(movieId: String): com.github.michaelbull.result.Result<MovierItemModel, Throwable>

    suspend fun createMovie(movie: MovierItemModel)
    suspend fun deleteMovie(movieId: String)
}