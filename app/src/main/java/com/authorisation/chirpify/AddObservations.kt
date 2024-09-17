package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class AddObservations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observations)

        // Initialize the Save Button
        val saveObservationButton = findViewById<Button>(R.id.saveObservationButton)

        // Set up the click listener
        saveObservationButton.setOnClickListener {
            // Start Homepage activity
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            // Optional: Finish current activity to prevent going back to it
            finish()
        }
    }
}
