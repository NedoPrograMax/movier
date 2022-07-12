package com.example.themovier.domain.repositories

import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.models.TvDetails

interface ApiRepo {
    suspend fun getMovies(
        query: String,
        movieType: String
    ): com.github.michaelbull.result.Result<MovieFromApi, Throwable>

    suspend fun getMovieInfo(movieId: String): com.github.michaelbull.result.Result<MovieDetails, Throwable>
    suspend fun getTvInfo(movieId: String): com.github.michaelbull.result.Result<TvDetails, Throwable>
}
