package com.example.themovier.data.repo

import com.example.themovier.data.apiservice.MovieApi
import com.example.themovier.data.mappers.MovieMapper
import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.models.TvDetails
import com.example.themovier.domain.repositories.ApiRepo
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(
    private val api: MovieApi
): ApiRepo{
   // val movieMapper = MovieMapper()
  override suspend fun getMovies(query: String, movieType: String): Result<MovieFromApi> {
        return kotlin.runCatching {
            api.getAllMovies(query = query, movieType = movieType)
        }
    }

   override suspend fun getMovieInfo(movieId: String): Result<MovieDetails> {
        return kotlin.runCatching {
            api.getMovieDetails(movieId = movieId)
        }
    }

   override suspend fun getTvInfo(movieId: String): Result<TvDetails> {
        return kotlin.runCatching {
            api.getTvDetails(movieId = movieId)
        }
    }
}