package com.seven.intershala.utils

import com.seven.intershala.model.NotesData

interface INotesClickListener {

    fun onDeleteNote(position: Int, data: NotesData)
    fun onEditNote(position: Int, data: NotesData)
}