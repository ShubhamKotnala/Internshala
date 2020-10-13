package com.seven.intershala.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seven.intershala.model.NotesData
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface NotesDataDao {

    @Insert
    fun insertNotes(feed: NotesData) : Completable

    @Update
    fun updateNotes(feed: NotesData) : Completable

    @Query("DELETE FROM ${NotesData.TABLE_NAME} WHERE id = :noteId")
    fun deleteNoteById(noteId: Int)  : Completable

    @Query("UPDATE  ${NotesData.TABLE_NAME} SET title = :title, description = :descr, date = :dateTime WHERE id = :id")
    fun updateData(id: Int, title: String?, descr: String?, dateTime: String?): Completable

    @Query("SELECT * FROM ${NotesData.TABLE_NAME}")
    fun getAllRecords():  Observable<List<NotesData>>
}