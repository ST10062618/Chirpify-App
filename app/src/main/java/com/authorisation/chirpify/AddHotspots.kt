package com.authorisation.chirpify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddHotspots : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var saveHotspotButton: Button
    private lateinit var logoImage: ImageView
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotspots)

        // Initialize views
        logoImage = findViewById(R.id.logoImage)
        titleText = findViewById(R.id.titleText)
        nameInput = findViewById(R.id.nameInput)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        saveHotspotButton = findViewById(R.id.saveHotspotButton)

        // Set click listener for the save button
        saveHotspotButton.setOnClickListener {
            addHotspot()
        }
    }

    private fun addHotspot() {
        val name = nameInput.text.toString().trim()
        val latitude = latitudeInput.text.toString().toDoubleOrNull()
        val longitude = longitudeInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString().trim()

        // Validate inputs
        if (name.isEmpty() || latitude == null || longitude == null || description.isEmpty()) {
            Snackbar.make(saveHotspotButton, "Please fill all fields with valid data", Snackbar.LENGTH_LONG).show()
            return
        }

        val newHotspot = Hotspot(name, latitude, longitude, description)

        // Make network call to add the hotspot
        val call = APiClient.hotspotApi.addHotspot(newHotspot)
        call.enqueue(object : Callback<HotspotResponse> {
            override fun onResponse(call: Call<HotspotResponse>, response: Response<HotspotResponse>) {
                if (response.isSuccessful) {
                    Snackbar.make(saveHotspotButton, "Hotspot added successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(saveHotspotButton, "Error: ${response.message()}", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<HotspotResponse>, t: Throwable) {
                Snackbar.make(saveHotspotButton, "Network error: ${t.message}", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
