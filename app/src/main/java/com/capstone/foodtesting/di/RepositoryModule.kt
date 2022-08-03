package com.capstone.foodtesting.di

import com.capstone.foodtesting.data.repository.MainRepository
import com.capstone.foodtesting.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

}