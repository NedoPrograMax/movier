package com.example.themovier.ui.di

import com.example.themovier.data.repo.ApiRepoImpl
import com.example.themovier.data.repo.FireRepoImpl
import com.example.themovier.domain.repositories.ApiRepo
import com.example.themovier.domain.repositories.FireRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindApiRepo(
        apiRepo: ApiRepoImpl,
    ): ApiRepo

    @Binds
    @Singleton
    abstract fun bindFireRepo(
        fireRepo: FireRepoImpl,
    ): FireRepo
}
