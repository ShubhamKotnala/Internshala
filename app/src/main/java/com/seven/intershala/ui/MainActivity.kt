package com.seven.intershala.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.seven.intershala.R
import com.seven.intershala.database.NotesDatabase
import com.seven.intershala.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var tvTitle: TextView
    lateinit var dataBaseInstance: NotesDatabase
    lateinit var imgBack: ImageView
    lateinit var imgLogout: ImageView
    private val RC_SIGN_IN = 9001
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "intershala_task"
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.activity_main)

        sharedPreferences =  getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        dataBaseInstance = NotesDatabase.getDatabaseInstance(this)
        viewModel.setInstanceOfDb(dataBaseInstance)

        tvTitle = findViewById(R.id.tv_title)
        imgBack = findViewById(R.id.img_back)
        imgLogout = findViewById(R.id.img_logout)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        tvTitle.text = getString(R.string.title_main)

        imgBack.setOnClickListener { super.onBackPressed() }
        imgLogout.setOnClickListener { logout() }

        openNoteListFragment()
    }

    private fun logout() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this
            ) {
                sharedPreferences.edit().clear().apply()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
    }

    private fun openNoteListFragment() {
        val mainFragment: NoteListFragment = NoteListFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame, mainFragment)
            .commit()
    }
}
