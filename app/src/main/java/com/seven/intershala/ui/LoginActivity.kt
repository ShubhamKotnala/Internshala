package com.seven.intershala.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.seven.intershala.R

class LoginActivity : AppCompatActivity() {

    private lateinit var progressbar: ProgressBar
    private lateinit var signInButton: SignInButton
    private val RC_SIGN_IN = 9001
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "intershala_task"
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)

        sharedPreferences =  getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        progressbar = findViewById(R.id.progress_circular)
        signInButton = findViewById(R.id.sign_in_button)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult( signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val googleId = account?.id ?: ""
            val emailId = account?.email ?: ""
            sharedPreferences.edit {
                putString("email", emailId)
                commit()
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: ApiException) {
            Log.e("failed code=", e.statusCode.toString())
        }
    }
}
