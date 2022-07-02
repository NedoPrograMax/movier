package com.example.themovier.data.movies.network

import com.example.themovier.constants.Constants
import com.example.themovier.model.MovieDetails
import com.example.themovier.model.MovieFromApi
import com.example.themovier.model.TvDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MovieApi {
    @GET("search/{MOVIE_TYPE}")
    suspend fun getAllMovies(
        @Path("MOVIE_TYPE") movieType: String,
        @Query("api_key") apiKey : String = Constants.API_KEY,
        @Query("query") query: String
    ): MovieFromApi

    @GET("movie/{MOVIE_ID}")
    suspend fun getMovieDetails(
    //    @Path("MOVIE_TYPE") movieType: String,
    @Path("MOVIE_ID") movieId: String,
    @Query("api_key") apiKey : String = Constants.API_KEY
    ): MovieDetails

    @GET("tv/{MOVIE_ID}")
    suspend fun getTvDetails(
        //    @Path("MOVIE_TYPE") movieType: String,
        @Path("MOVIE_ID") movieId: String,
        @Query("api_key") apiKey : String = Constants.API_KEY
    ): TvDetails
}