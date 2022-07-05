package com.example.themovier.data.di

import com.example.themovier.data.apiservice.MovieApi
import com.example.themovier.data.constants.Constants
import com.example.themovier.data.repo.ApiRepoImpl
import com.example.themovier.data.repo.FireRepoImpl
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

  /*  @Singleton
    @Provides
    fun provideFireRepository() = FireRepoImpl(queryUser =
    FirebaseFirestore.getInstance().collection("users"))

   */

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

  /* @Singleton
    @Provides
    fun provideApiRepository(api: MovieApi) = ApiRepoImpl(api)

   */
}
