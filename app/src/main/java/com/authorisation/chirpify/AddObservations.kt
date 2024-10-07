package com.authorisation.chirpify

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import java.util.*

class AddObservations : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var speciesInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var saveObservationButton: Button
    private lateinit var mMap: GoogleMap
    private var selectedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observations)

        // Initialize inputs and button
        speciesInput = findViewById(R.id.speciesInput)
        notesInput = findViewById(R.id.notesInput)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        saveObservationButton = findViewById(R.id.saveObservationButton)

        // Set up the map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set click listener for the save button
        saveObservationButton.setOnClickListener {
            saveObservation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener { latLng ->
            selectedLatLng = latLng
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))

            // Fill in the latitude and longitude fields
            latitudeInput.setText(latLng.latitude.toString())
            longitudeInput.setText(latLng.longitude.toString())

            // Fetch and display the address
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) ?: emptyList()

            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                notesInput.setText("Observed at: $address")
            }
        }
    }

    private fun saveObservation() {
        val species = speciesInput.text.toString().trim()
        val notes = notesInput.text.toString().trim()
        val latitude = latitudeInput.text.toString().trim()
        val longitude = longitudeInput.text.toString().trim()

        // Check if all fields are filled
        if (species.isEmpty() || notes.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a formatted observation string
        val observation = "$species at ($latitude, $longitude): $notes"

        // Save observation to SharedPreferences
        val sharedPreferences = getSharedPreferences("BirdObservations", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingObservations = sharedPreferences.getStringSet("observations", mutableSetOf()) ?: mutableSetOf()
        existingObservations.add(observation)

        editor.putStringSet("observations", existingObservations)
        editor.apply()

        // Optionally, navigate back to the homepage or to the view page
        Toast.makeText(this, "Observation saved", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Homepage::class.java)
        startActivity(intent)
        finish() // Optional: finish this activity so it can't be returned to
    }
}
