package com.example.themovier.di

import com.example.themovier.data.movie.MovieDataSourceImpl
import com.example.themovier.data.repo.ApiRepoImpl
import com.example.themovier.data.user.UserDataSourceImpl
import com.example.themovier.domain.movie.MovieDataSource
import com.example.themovier.domain.repositories.ApiRepo
import com.example.themovier.domain.user.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindApiRepo(
        apiRepo: ApiRepoImpl,
    ): ApiRepo

    @Binds
    @Singleton
    abstract fun bindMovieDatasource(
        movieDataSourceImpl: MovieDataSourceImpl,
    ): MovieDataSource

    @Binds
    @Singleton
    abstract fun bindUserDataSource(
        userDataSource: UserDataSourceImpl,
    ): UserDataSource
}
