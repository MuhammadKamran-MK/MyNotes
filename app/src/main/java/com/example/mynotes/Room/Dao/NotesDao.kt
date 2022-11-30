package com.example.mynotes.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mynotes.Room.Entities.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): LiveData<List<Notes>>

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(notes: Notes)

    @Update
    suspend fun updateNote(notes: Notes)

}