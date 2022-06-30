package com.example.themovier.repository

import com.example.themovier.data.DataOrException
import com.example.themovier.model.MovieDetails
import com.example.themovier.model.MovieFromApi
import com.example.themovier.model.TvDetails
import com.example.themovier.network.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(private val api: MovieApi) {

        suspend fun getMovies(query: String, movieType: String): DataOrException<MovieFromApi, Exception> {
             val dataOrException = DataOrException<MovieFromApi, Exception>()
             try{
                 dataOrException.data = api.getAllMovies(query = query, movieType = movieType)
            }catch (e:Exception){
                dataOrException.e = e
            }
            return dataOrException
        }

    suspend fun getMovieInfo(movieId: String): DataOrException<MovieDetails, Exception> {
        val dataOrException = DataOrException<MovieDetails, Exception>()
        try{
            dataOrException.data = api.getMovieDetails(movieId = movieId)
        }catch (e:Exception){
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getTvInfo(movieId: String): DataOrException<TvDetails, Exception> {
        val dataOrException = DataOrException<TvDetails, Exception>()
        try{
            dataOrException.data = api.getTvDetails(movieId = movieId)
        }catch (e:Exception){
            dataOrException.e = e
        }
        return dataOrException
    }

}
