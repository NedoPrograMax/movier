package com.example.themovier.network

import com.example.themovier.constants.Constants
import com.example.themovier.model.MovieDetails
import com.example.themovier.model.MovieFromApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MovieApi {
    @GET("search/movie")
    suspend fun getAllMovies(
        @Query("api_key") apiKey : String = Constants.API_KEY,
        @Query("query") query: String
    ): MovieFromApi

    @GET("movie/{MOVIE_ID}")
    suspend fun getMovieDetails(
    @Path("MOVIE_ID") movieId: String,
    @Query("api_key") apiKey : String = Constants.API_KEY
    ): MovieDetails
}