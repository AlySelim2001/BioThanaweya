package com.example.di

import com.example.data.repository.AiRepositoryImpl
import com.example.data.repository.BiologyRepositoryImpl
import com.example.domain.repository.AiRepository
import com.example.domain.repository.BiologyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository implementations to their domain interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBiologyRepository(
        impl: BiologyRepositoryImpl
    ): BiologyRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: AiRepositoryImpl
    ): AiRepository
}
