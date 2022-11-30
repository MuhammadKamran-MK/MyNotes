package com.example.mynotes.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mynotes.Repository.NotesRepository
import com.example.mynotes.Room.Database.NotesDatabase
import com.example.mynotes.Room.Entities.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// AndroidViewModel run for whole application
// If an activity destroyed it will not destroy
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository

    // init means if you write a code inside it
    // that code will run first
    init {
        val dao = NotesDatabase.getDatabase(application).myNotesDao()
        repository = NotesRepository(dao)
    }

    fun getNotes(): LiveData<List<Notes>> = repository.getAllNotes()

    fun deleteNotes(id: Int) = viewModelScope.launch { repository.deleteNote(id) }

    fun insertNotes(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(notes)
        }
    }

    fun updateNotes(notes: Notes) = viewModelScope.launch { repository.updateNote(notes) }

}