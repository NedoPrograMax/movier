package com.example.themovier.di

import com.example.themovier.constants.Constants
import com.example.themovier.data.movies.network.MovieApi
import com.example.themovier.repository.FireRepository
import com.example.themovier.repository.MovieRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideFireRepository() = FireRepository(queryUser =
    FirebaseFirestore.getInstance().collection("users"))

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(api: MovieApi) = MovieRepository(api)


}