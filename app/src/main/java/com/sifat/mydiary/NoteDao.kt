package com.sifat.mydiary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert
    fun insertData(note: Note)

    @Update
    fun UpdateData(note: Note)

    @Delete
    fun DeleteData(note: Note)

    @Query("SELECT * FROM Note")
    fun getalldata(): List<Note>


    @Query("SELECT * FROM Note WHERE id IN (:ids)")
    fun loadAllByIds(ids: List<Int>): List<Note>
}