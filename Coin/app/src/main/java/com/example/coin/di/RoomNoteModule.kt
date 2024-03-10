package com.example.coin.di

import android.content.Context
import androidx.room.Room
import com.example.coin.daos.NoteDao
import com.example.coin.repository.room.NotesDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomNoteModule {
    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): NoteDao {
        return Room.databaseBuilder(
            context,
            NotesDataBase::class.java, "roomDBNotes"
        )
            .allowMainThreadQueries()
            .build()
            .notesDao()
    }
}