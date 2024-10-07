package com.authorisation.chirpify

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.*

class AddObservations : AppCompatActivity() {

    private lateinit var speciesInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var saveObservationButton: Button
    private lateinit var useCurrentLocationButton: Button

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observations)

        // Initialize inputs and buttons
        speciesInput = findViewById(R.id.speciesInput)
        notesInput = findViewById(R.id.notesInput)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        saveObservationButton = findViewById(R.id.saveObservationButton)
        useCurrentLocationButton = findViewById(R.id.useCurrentLocationButton)

        // Set click listener for the save button
        saveObservationButton.setOnClickListener {
            saveObservation()
        }

        // Set click listener for using the current location
        useCurrentLocationButton.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitudeInput.setText(location.latitude.toString())
                longitudeInput.setText(location.longitude.toString())
                fetchAndDisplayAddress(location)
            } else {
                Toast.makeText(this, "Could not retrieve current location. Please enter coordinates manually.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location. Please enter coordinates manually.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAndDisplayAddress(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1) ?: emptyList()

            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                notesInput.setText("Observed at: $address")
            } else {
                Toast.makeText(this, "No address found for this location.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to get address. Please use coordinates manually.", Toast.LENGTH_SHORT).show()
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
