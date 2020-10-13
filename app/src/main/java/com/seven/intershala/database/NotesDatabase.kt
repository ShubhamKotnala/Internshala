package com.seven.intershala.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.seven.intershala.model.NotesData

const val DB_VERSION = 1

const val DB_NAME = "notes_database.db"

@Database(entities = [NotesData::class], version = DB_VERSION)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun feedDataDao(): NotesDataDao

    companion object {
        @Volatile
        private var databaseInstance: NotesDatabase? = null

        fun getDatabaseInstance(mContext: Context): NotesDatabase =
            databaseInstance ?: synchronized(this) {
                databaseInstance ?: buildDatabaseInstance(mContext).also {
                    databaseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, NotesDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
