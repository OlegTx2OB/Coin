package com.example.coin.di

import com.example.coin.repository.room.CategoryRepository
import com.example.coin.repository.room.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryCategoryModule {
    @Singleton
    @Provides
    fun provideNoteRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository {
        return categoryRepositoryImpl
    }
}