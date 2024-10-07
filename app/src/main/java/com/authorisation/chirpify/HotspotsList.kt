package com.authorisation.chirpify

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HotspotsList : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var refreshEBirdHotspotsButton: Button
    private lateinit var hotspotsListView: ListView
    private lateinit var backToHomepageButton: Button

    private lateinit var hotspotsRepository: HotspotsRepository
    private val hotspotsList: MutableList<String> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)

        // Initialize views
        logoImage = findViewById(R.id.logoImage)
        titleText = findViewById(R.id.titleText)
        refreshEBirdHotspotsButton = findViewById(R.id.refreshEBirdHotspotsButton)
        hotspotsListView = findViewById(R.id.hotspotsListView)
        backToHomepageButton = findViewById(R.id.backToHomepageButton)

        // Initialize repository
        hotspotsRepository = HotspotsRepository(this)

        // Set up ListView adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hotspotsList)
        hotspotsListView.adapter = adapter

        // Load hotspots
        loadHotspots()

        // Refresh button click listener
        refreshEBirdHotspotsButton.setOnClickListener {
            loadHotspots()
        }

        // Back to homepage button click listener
        backToHomepageButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun loadHotspots() {
        // Replace with actual latitude and longitude
        val latitude = 0.0
        val longitude = 0.0
        val maxDistance = 10 // Example distance in km

        lifecycleScope.launch {
            val hotspots = withContext(Dispatchers.IO) {
                hotspotsRepository.fetchEBirdHotspots(latitude, longitude, maxDistance)
            }

            if (hotspots.isNotEmpty()) {
                hotspotsList.clear()
                hotspots.forEach {
                    hotspotsList.add("${it.name}: ${it.latitude}, ${it.longitude} - ${it.description}")
                }
                adapter.notifyDataSetChanged()
            } else {
                Log.e("HotspotsList", "No hotspots found or error occurred.")
            }
        }
    }
}
