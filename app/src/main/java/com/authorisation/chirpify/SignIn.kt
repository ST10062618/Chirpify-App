package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in) // Ensure this matches your layout file

        // Find the "Not Registered?" text view
        val notRegisteredText = findViewById<TextView>(R.id.notRegisteredText)

        // Set onClickListener to navigate to the Sign-Up page
        notRegisteredText.setOnClickListener {
            val intent = Intent(this, SignUp::class.java) // Navigate to SignUp activity
            startActivity(intent)
        }

        // Find the "Sign In" button
        val signInButton = findViewById<Button>(R.id.signInButton)

        // Set onClickListener for the Sign In button
        signInButton.setOnClickListener {
            // Create an Intent to navigate to the Homepage
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }
    }
}
