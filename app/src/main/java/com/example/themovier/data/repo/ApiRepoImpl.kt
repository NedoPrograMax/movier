package com.example.themovier.data.repo

import com.example.themovier.data.apiservice.MovieApi
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.models.TvDetails
import com.example.themovier.domain.repositories.ApiRepo
import com.github.michaelbull.result.runCatching
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(
    private val api: MovieApi
): ApiRepo {
    override suspend fun getMovies(
        query: String,
        movieType: String
    ): com.github.michaelbull.result.Result<MovieFromApi, Throwable> {
        return runCatching {
            api.getAllMovies(query = query, movieType = movieType)
        }
    }

    override suspend fun getMovieInfo(movieId: String): com.github.michaelbull.result.Result<MovieDetails, Throwable> {
        return runCatching {
            api.getMovieDetails(movieId = movieId)
        }
    }

    override suspend fun getTvInfo(movieId: String): com.github.michaelbull.result.Result<TvDetails, Throwable> {
        return runCatching {
            api.getTvDetails(movieId = movieId)
        }
    }
}
