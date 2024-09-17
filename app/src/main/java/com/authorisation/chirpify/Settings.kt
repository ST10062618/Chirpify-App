package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views using findViewById
        val saveChangesButton: Button = findViewById(R.id.saveChangesButton)
        val signOutButton: Button = findViewById(R.id.signOutButton)

        // Save Changes Button
        saveChangesButton.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()  // Optionally, finish the activity to remove it from the back stack
        }

        // Sign Out Button
        signOutButton.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()  // Optionally, finish the activity to remove it from the back stack
        }
    }
}
