package com.example.mynotes.Repository

import androidx.lifecycle.LiveData
import com.example.mynotes.Room.Dao.NotesDao
import com.example.mynotes.Room.Entities.Notes

class NotesRepository(val notesDao: NotesDao) {

    fun getAllNotes() : LiveData<List<Notes>> {
        return notesDao.getAllNotes()
    }

    suspend fun deleteNote(id:Int) {
        notesDao.deleteNote(id)
    }

    suspend fun insertNote(notes: Notes) {
        notesDao.insertNote(notes)
    }

    suspend fun updateNote(notes: Notes) {
        notesDao.updateNote(notes)
    }

}