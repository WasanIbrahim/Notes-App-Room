package com.example.notesapp_room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true) val pk: Int,
    val note: String
)