package com.seven.intershala.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.data.DataHolder
import com.seven.intershala.R
import com.seven.intershala.model.NotesData
import com.seven.intershala.utils.INotesClickListener
import com.seven.intershala.utils.Utils


class NotesAdapter(val notes: ArrayList<NotesData>, var iNotesClickListener: INotesClickListener) : RecyclerView.Adapter<HistoryViewHolder>() {

    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)

        context = parent.context

        return HistoryViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        return holder.bind(notes[position], iNotesClickListener)
    }

    fun updateList(list: List<NotesData>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }
}

class HistoryViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.title)
    private val tvDesc: TextView = itemView.findViewById(R.id.description)
    private val tvDate: TextView = itemView.findViewById(R.id.date)
    private val imgEdit: ImageView = itemView.findViewById(R.id.bt_edit)
    private val imgDelete: ImageView = itemView.findViewById(R.id.bt_delete)

    fun bind(notes: NotesData, iNotesClickListener: INotesClickListener) {
        tvTitle.text = notes.title
        tvDesc.text = notes.description
        tvDate.text = Utils.getConvertedDate(notes.date)

        imgEdit.setOnClickListener { iNotesClickListener.onEditNote(adapterPosition, notes) }
        imgDelete.setOnClickListener { iNotesClickListener.onDeleteNote(adapterPosition, notes) }
    }
}