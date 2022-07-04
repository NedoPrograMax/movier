package com.example.themovier.domain.repositories

import com.example.themovier.domain.models.MovieDetails
import com.example.themovier.domain.models.MovieFromApi
import com.example.themovier.domain.models.TvDetails

interface ApiRepo {
    suspend fun getMovies(query: String, movieType: String): Result<MovieFromApi>
    suspend fun getMovieInfo(movieId: String): Result<MovieDetails>
    suspend fun getTvInfo(movieId: String): Result<TvDetails>
}