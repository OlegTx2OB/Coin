package com.example.coin.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.coin.data.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}