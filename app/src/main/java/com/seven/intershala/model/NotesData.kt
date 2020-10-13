package com.seven.intershala.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = NotesData.TABLE_NAME)
class NotesData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0 ,
    @ColumnInfo(name = "title") var title: String? = "",
    @ColumnInfo(name = "description") var description: String? = "",
    @ColumnInfo(name = "date") var date: String? = ""
): Serializable
{
    companion object {
        const val TABLE_NAME = "feeds"
    }
}
