package com.example.coin.di

import com.example.coin.repository.room.NoteRepository
import com.example.coin.repository.room.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryNoteModule {
    @Singleton
    @Provides
    fun provideNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository {
        return noteRepositoryImpl
    }
}