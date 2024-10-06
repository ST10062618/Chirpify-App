package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class viewBirdsObservation : AppCompatActivity() {

    private lateinit var observationsListView: ListView
    private lateinit var returnHomeButton: Button

    // This will store your bird observations
    private val birdObservations: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_birds_observation)

        // Initialize views
        observationsListView = findViewById(R.id.observationsListView)
        returnHomeButton = findViewById(R.id.returnHomeButton)

        // Load bird observations from SharedPreferences
        loadBirdObservations()

        // Set up the ListView with an ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, birdObservations)
        observationsListView.adapter = adapter

        // Set return button click listener
        returnHomeButton.setOnClickListener {
            // Navigate back to the homepage
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish() // Optional: finish this activity so it can't be returned to
        }
    }

    private fun loadBirdObservations() {
        // Retrieve observations from SharedPreferences
        val sharedPreferences = getSharedPreferences("BirdObservations", MODE_PRIVATE)
        val existingObservations = sharedPreferences.getStringSet("observations", mutableSetOf())

        // Add retrieved observations to the list
        existingObservations?.let {
            birdObservations.addAll(it)
        }
    }
}
