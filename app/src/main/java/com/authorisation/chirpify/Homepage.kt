package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.widget.Button

class Homepage : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize buttons using findViewById
        val addObservationButton = findViewById<Button>(R.id.addObservationButton)
        val viewObservationButton = findViewById<Button>(R.id.viewObservationButton)
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        // Set click listener to navigate to SettingsActivity
        settingsButton.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        // Set button click listeners
        addObservationButton.setOnClickListener {
            val intent = Intent(this, AddObservations::class.java)
            startActivity(intent)
        }

        viewObservationButton.setOnClickListener {
            val intent = Intent(this, ViewBirdsObservation::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish() // Optional: Call finish() to remove this activity from the back stack
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_homepage)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
