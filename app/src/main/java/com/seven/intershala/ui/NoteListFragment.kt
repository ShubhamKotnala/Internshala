package com.seven.intershala.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.data.DataHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seven.intershala.R
import com.seven.intershala.adapter.NotesAdapter
import com.seven.intershala.model.NotesData
import com.seven.intershala.utils.INotesClickListener
import com.seven.intershala.viewmodel.MainViewModel

class NoteListFragment : Fragment(), INotesClickListener {

    private lateinit var model: MainViewModel
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var rvNotes: RecyclerView
    private lateinit var imgNodata: ImageView
    private lateinit var searchView: SearchView
    private  var listNotes : ArrayList<NotesData> = ArrayList<NotesData>()
    private  var listNotesMain : ArrayList<NotesData> = ArrayList<NotesData>()
    private lateinit var noteListAdapter : NotesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_note_list, container, false)

        initializeWidgets(root)

        return root
    }

    private fun initializeWidgets(root: View) {
        model = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        fabAddNote = root.findViewById(R.id.fab_add_note)
        rvNotes = root.findViewById(R.id.rv_notes)
        imgNodata = root.findViewById(R.id.background_image)
        searchView = root.findViewById(R.id.searchView)
        (activity as MainActivity?)!!.imgBack.visibility = View.GONE

        model.offlineDataList.observe(viewLifecycleOwner, Observer<ArrayList<NotesData>> { response ->
            if (response == null) {
                Toast.makeText(this.context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            } else {
                if (response.isEmpty())
                    Toast.makeText(this.context, getString(R.string.no_notes), Toast.LENGTH_SHORT).show()
                listNotesMain = response
                setNotesAdapter(response)
            }
        })

        fabAddNote.setOnClickListener { addNote() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrBlank()) {
                    noteListAdapter.updateList(listNotesMain)
                    setNoDataVis(listNotesMain.isEmpty())
                    return false
                }

                filter(newText)
                return false
            }
        })

        getNotes()
    }

    fun filter(text: CharSequence) {
        val temp: MutableList<NotesData> = ArrayList()
        for (note in listNotes) {
            if (note.title!!.contains(text) || note.description!!.contains(text)) {
                temp.add(note)
            }
        }

        noteListAdapter.updateList(temp)
        setNoDataVis(temp.isEmpty())
    }

    private fun setNoDataVis(boolean: Boolean) {
        if (boolean) imgNodata.visibility = View.VISIBLE
        else imgNodata.visibility = View.GONE
    }

    private fun addNote() {
        val mainFragment = AddNoteFragment()

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.frame, mainFragment)
            .addToBackStack("AddNoteFragment")
            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
            .commit()
    }

    private fun editNote(notesData: NotesData) {
        val mainFragment = AddNoteFragment()

        val bundle = Bundle()
        bundle.putSerializable("data", notesData)
        bundle.putBoolean("isedit", true)

        mainFragment.arguments = bundle

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.frame, mainFragment)
            .addToBackStack("AddNoteFragment")
            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
            .commit()
    }

    private fun getNotes() {
        model.getSavedData()
    }

    private fun setNotesAdapter(list: ArrayList<NotesData>) {
        listNotes.clear()
        listNotes.addAll(list)
        setNoDataVis(listNotes.isEmpty())
        noteListAdapter = NotesAdapter(listNotes, this)

        val manager = LinearLayoutManager(activity,  LinearLayoutManager.VERTICAL, false)
        rvNotes.layoutManager = manager
        rvNotes.adapter = noteListAdapter
    }

    override fun onDeleteNote(position: Int, data: NotesData) {
        model.deleteNote(data)
        listNotes.removeAt(position)
        noteListAdapter.notifyItemRemoved(position)
        noteListAdapter.notifyItemRangeRemoved(position, listNotes.size)
    }

    override fun onEditNote(position: Int, data: NotesData) {
        editNote(data)
    }
}