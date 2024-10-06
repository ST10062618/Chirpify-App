package com.authorisation.chirpify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddObservations : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var speciesInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveObservationButton: Button
    private lateinit var map: GoogleMap
    private var selectedLocation: LatLng? = null // Store the selected location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observations)

        // Initialize inputs and button
        speciesInput = findViewById(R.id.speciesInput)
        notesInput = findViewById(R.id.notesInput)
        saveObservationButton = findViewById(R.id.saveObservationButton)

        // Initialize the map fragment
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        // Set click listener for the save button
        saveObservationButton.setOnClickListener {
            saveObservation()
        }
    }

    // OnMapReadyCallback implementation
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val johannesburg = LatLng(-26.2041, 28.0473) // Default location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(johannesburg, 10f))

        // Set a click listener to place a marker on the map
        map.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            map.clear() // Clear previous markers
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        }
    }

    private fun saveObservation() {
        val location = selectedLocation?.let { "${it.latitude}, ${it.longitude}" } ?: return
        val species = speciesInput.text.toString().trim()
        val notes = notesInput.text.toString().trim()

        // Check if all fields are filled
        if (species.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a formatted observation string
        val observation = "$species at $location: $notes"

        // Save observation to SharedPreferences
        val sharedPreferences = getSharedPreferences("BirdObservations", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingObservations = sharedPreferences.getStringSet("observations", mutableSetOf())
        existingObservations?.add(observation)

        editor.putStringSet("observations", existingObservations)
        editor.apply()

        // Optionally, navigate back to the homepage or to the view page
        Toast.makeText(this, "Observation saved", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Homepage::class.java)
        startActivity(intent)
        finish() // Optional: finish this activity so it can't be returned to
    }
}
