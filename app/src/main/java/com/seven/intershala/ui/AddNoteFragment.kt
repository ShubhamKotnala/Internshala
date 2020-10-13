package com.seven.intershala.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seven.intershala.R
import com.seven.intershala.database.NotesDatabase
import com.seven.intershala.model.NotesData
import com.seven.intershala.viewmodel.SaveNoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment() {

    private lateinit var model: SaveNoteViewModel
    private lateinit var btSave: Button
    lateinit var dataBaseInstance: NotesDatabase
    private lateinit var etTitle: AppCompatEditText
    private lateinit var etDesc: AppCompatEditText
    private lateinit var notesData: NotesData
    private  var isEdit: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_add_note, container, false)

        initializeWidgets(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = ViewModelProviders.of(this).get(SaveNoteViewModel::class.java)

        dataBaseInstance = NotesDatabase.getDatabaseInstance(context!!)
        model.setInstanceOfDb(dataBaseInstance)

        model.saveDataListener.observe(this.viewLifecycleOwner, observer)
    }

    private fun initializeWidgets(root: View) {
        btSave = root.findViewById(R.id.bt_save)
        etDesc = root.findViewById(R.id.et_desc)
        etTitle = root.findViewById(R.id.et_title)

        (activity as MainActivity?)!!.imgBack.visibility = View.VISIBLE

        btSave.setOnClickListener { saveNote() }

        val bundle = this.arguments
        if (bundle != null) {
            notesData = (bundle.getSerializable("data") as NotesData?)!!
            isEdit = bundle.getBoolean("isedit")
            etTitle.setText(notesData.title)
            etDesc.setText(notesData.description)
        }
    }

    val observer = Observer<Boolean> {  response ->
        if (response) {
            Toast.makeText(this.context, getString(R.string.sucess), Toast.LENGTH_SHORT).show()
            etTitle.text?.clear()
            etDesc.text?.clear()
        } else {
            Toast.makeText(this.context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()

        model.saveDataListener.removeObserver(observer)
    }

    private fun saveNote() {
        if (etTitle.text.isNullOrBlank()) {
            Toast.makeText(activity, "Please enter title!", Toast.LENGTH_SHORT).show()
            return
        }
        val date = SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH).format(Date())

        val notes_Data = NotesData()

        notes_Data.title = etTitle.text.toString()
        notes_Data.description = etDesc.text.toString()
        notes_Data.date = date

        if (isEdit) {
            notes_Data.id = notesData.id
            model.updateNote(notes_Data)
        }
        else
            model.saveDataIntoDb(notes_Data)
    }
}