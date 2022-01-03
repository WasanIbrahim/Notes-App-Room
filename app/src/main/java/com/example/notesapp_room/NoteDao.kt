package com.example.notesapp_room

import androidx.room.*


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Notes)

    @Query("SELECT * FROM notes ORDER BY pk ASC")
    fun getNote(): List<Notes>


    @Update
    suspend fun updateNote(note:Notes)


    @Delete
    suspend fun deleteNote(note:Notes)
}