package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class HotspotsList : AppCompatActivity() {

    private lateinit var hotspotsListView: ListView
    private lateinit var addHotspotButton: Button
    private lateinit var refreshEBirdHotspotsButton: Button
    private lateinit var backToHomepageButton: Button
    private lateinit var hotspotsRepository: HotspotsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)

        hotspotsListView = findViewById(R.id.hotspotsListView)
        addHotspotButton = findViewById(R.id.addHotspotButton)
        refreshEBirdHotspotsButton = findViewById(R.id.refreshEBirdHotspotsButton)
        backToHomepageButton = findViewById(R.id.backToHomepageButton)

        hotspotsRepository = HotspotsRepository(this)

        // Load hotspots when the activity is created
        loadHotspots()

        // Set click listeners
        addHotspotButton.setOnClickListener {
            // Start AddHotspotActivity
            val intent = Intent(this, AddHotspots::class.java)
            startActivity(intent)
        }

        refreshEBirdHotspotsButton.setOnClickListener {
            loadHotspots()
        }

        backToHomepageButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun loadHotspots() {
        val ebirdHotspots = hotspotsRepository.fetchEBirdHotspots()
        val userHotspots = hotspotsRepository.fetchUserHotspots()
        val allHotspots = ebirdHotspots + userHotspots

        if (allHotspots.isNotEmpty()) {
            val hotspotNames = allHotspots.map { it.name }
            hotspotsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hotspotNames)
            Snackbar.make(refreshEBirdHotspotsButton, "Found ${allHotspots.size} hotspots", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(refreshEBirdHotspotsButton, "No hotspots found", Snackbar.LENGTH_LONG).show()
        }
    }
}
