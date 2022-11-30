package com.example.mynotes.Room.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// on below line we are specifying our table name
@Entity(tableName = "Notes")
data class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String,
    val date: String
)
