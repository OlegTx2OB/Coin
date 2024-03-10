package com.example.coin.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.coin.R
import com.example.coin.daos.NoteDao
import com.example.coin.data.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}