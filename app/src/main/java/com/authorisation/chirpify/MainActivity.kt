package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Set the splash screen layout

        // Delay to show splash screen for 2 seconds
        Handler().postDelayed({
            // Start SignIn activity after the delay
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()  // Finish the splash activity so the user cannot return to it
        }, 2000)  // 2000 milliseconds delay
    }
}
