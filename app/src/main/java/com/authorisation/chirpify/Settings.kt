package com.authorisation.chirpify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.ToggleButton

class Settings : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

        // Find views
        val distanceInput = findViewById<TextInputEditText>(R.id.distanceInput)
        val systemToggle = findViewById<ToggleButton>(R.id.systemToggle)
        val saveChangesButton = findViewById<Button>(R.id.saveChangesButton)

        // Load saved preferences when settings screen opens
        loadPreferences(distanceInput, systemToggle)

        // Handle save changes button click
        saveChangesButton.setOnClickListener {
            val distanceText = distanceInput.text.toString()

            // Validate and save max distance
            if (distanceText.isNotEmpty()) {
                saveMaxDistance(distanceText.toInt())
            } else {
                Toast.makeText(this, "Please enter a valid distance", Toast.LENGTH_SHORT).show()
            }

            // Save the selected system (Metric or Imperial)
            val isMetric = systemToggle.isChecked
            saveMeasurementSystem(isMetric)

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()

            // Navigate to Homepage after saving changes
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()  // Optionally finish the current activity
        }
    }

    // Save the max distance input to SharedPreferences
    private fun saveMaxDistance(maxDistance: Int) {
        val editor = sharedPref.edit()
        editor.putInt("MAX_DISTANCE", maxDistance)
        editor.apply()
    }

    // Save the measurement system toggle state to SharedPreferences
    private fun saveMeasurementSystem(isMetric: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("IS_METRIC", isMetric)
        editor.apply()
    }

    // Load saved preferences for max distance and system toggle
    private fun loadPreferences(distanceInput: TextInputEditText, systemToggle: ToggleButton) {
        // Load and set max distance
        val savedDistance = sharedPref.getInt("MAX_DISTANCE", 10)  // Default value is 10 km
        distanceInput.setText(savedDistance.toString())

        // Load and set measurement system
        val isMetric = sharedPref.getBoolean("IS_METRIC", true)  // Default is Metric system
        systemToggle.isChecked = isMetric
        systemToggle.text = if (isMetric) "Metric" else "Imperial"
    }
}
