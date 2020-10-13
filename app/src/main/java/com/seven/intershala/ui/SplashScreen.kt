package com.seven.intershala.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.seven.intershala.R

class SplashScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "intershala_task"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences =  getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        switchToHome()
    }

    private fun switchToHome() {
        Handler().postDelayed({
            if (sharedPreferences.getString("email", "").equals("", ignoreCase = false)) {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}
