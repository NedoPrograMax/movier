package com.example.themovier.domain.movie

import com.example.themovier.domain.models.MovierItemModel
import com.example.themovier.domain.models.TotalMovie
import com.github.michaelbull.result.Result

interface MovieDataSource {
    suspend fun getUserMovies(userId: String): com.github.michaelbull.result.Result<List<MovierItemModel>, Throwable>
    suspend fun getMovie(movieId: String): com.github.michaelbull.result.Result<MovierItemModel, Throwable>

    suspend fun createMovie(movie: MovierItemModel)
    suspend fun deleteMovie(movieId: String)

    suspend fun createTotalMovie(totalMovie: TotalMovie): com.github.michaelbull.result.Result<Unit, Exception>
    suspend fun getTotalMovie(
        idDb: String,
        type: String,
    ): com.github.michaelbull.result.Result<TotalMovie, Exception>

    suspend fun updateTotalMovieData(
        totalMovieHashMap: Map<String, Any>,
        idDb: String,
        type: String,
    )

}