package com.authorisation.chirpify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)  // Update with your actual sign-in layout

        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val notRegisteredText = findViewById<TextView>(R.id.notRegisteredText)  // Reference to the "Not Registered?" TextView

        signInButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Retrieve stored credentials from SharedPreferences
            val sharedPref = getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE)
            val savedEmail = sharedPref.getString("EMAIL", "")
            val savedPassword = sharedPref.getString("PASSWORD", "")

            if (email == savedEmail && password == savedPassword) {
                Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()

                // Navigate to the Homepage Activity
                val intent = Intent(this, Homepage::class.java)
                startActivity(intent)
                finish()  // Optionally prevent returning to the sign-in screen
            } else {
                Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Not Registered text click
        notRegisteredText.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}
