package com.example.mynotes.Room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.Room.Dao.NotesDao
import com.example.mynotes.Room.Entities.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun myNotesDao(): NotesDao

    companion object {

        var Database_Name = "My Notes"

        @Volatile
        private var INSTANCE: NotesDatabase? = null // This instance hold our database

        fun getDatabase(context: Context): NotesDatabase {

            if (INSTANCE == null) {

                synchronized(this) {

                    INSTANCE =
                        Room.databaseBuilder(context, NotesDatabase::class.java, Database_Name)
                            .build()

                }

            }

            return INSTANCE!!

        }
    }
}