package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ListView

class ViewBirdsObservation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_birds_observation)

        // Initialize the Return to Home Button
        val returnHomeButton = findViewById<Button>(R.id.returnHomeButton)

        // Set up the click listener
        returnHomeButton.setOnClickListener {
            // Start Homepage activity
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            // Optional: Finish current activity to prevent going back to it
            finish()
        }
    }
}
