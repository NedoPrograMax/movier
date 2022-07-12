package com.example.themovier.data.repo

import com.example.themovier.data.apiservice.MovieApi
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.models.TvDetails
import com.example.themovier.domain.repositories.ApiRepo
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(
    private val api: MovieApi,
) : ApiRepo {
    override suspend fun getMovies(
        query: String,
        movieType: String,
    ): com.github.michaelbull.result.Result<MovieFromApi, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val movies = api.getAllMovies(query = query, movieType = movieType)
                Ok(movies)
            } catch (e: Exception) {
                Err(e)
            }
        }


    override suspend fun getMovieInfo(movieId: String): com.github.michaelbull.result.Result<MovieDetails, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val movie = api.getMovieDetails(movieId = movieId)
                Ok(movie)
            } catch (e: Exception) {
                Err(e)
            }
        }

    override suspend fun getTvInfo(movieId: String): com.github.michaelbull.result.Result<TvDetails, Throwable> =
        withContext(Dispatchers.IO) {
            try {
                val tv = api.getTvDetails(movieId = movieId)
                Ok(tv)
            } catch (e: Exception) {
                Err(e)
            }
        }
}
