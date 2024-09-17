package com.authorisation.chirpify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up) // Make sure this layout matches your sign-up page

        // Find the "Sign In" text view
        val signInText = findViewById<TextView>(R.id.signInText)

        // Set onClickListener to navigate to the Sign In page
        signInText.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }
}
