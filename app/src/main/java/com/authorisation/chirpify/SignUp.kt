package com.authorisation.chirpify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)  // Ensure layout matches your XML file

        // Get references to views
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<TextInputEditText>(R.id.confirmPasswordInput)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val signInText = findViewById<TextView>(R.id.signInText)

        // Handle Sign Up button click
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (validateInputs(email, password, confirmPassword)) {
                // Store credentials locally using SharedPreferences
                val sharedPref = getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("EMAIL", email)
                editor.putString("PASSWORD", password)
                editor.apply()

                // Show success message
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()

                // Navigate to Sign In page
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                finish()  // Optionally prevent returning to this screen
            }
        }

        // Navigate to Sign In screen when "Sign In" is clicked
        signInText.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    // Helper function to validate inputs
    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate password is not empty and at least 6 characters (can be adjusted)
        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate confirm password matches password
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
