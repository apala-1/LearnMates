package com.example.learnmates

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password) // Ensure this matches the XML file name

        // Get references to views
        val emailInput: EditText = findViewById(R.id.emailInput)
        val oldPasswordInput: EditText = findViewById(R.id.oldPasswordInput)
        val newPasswordInput: EditText = findViewById(R.id.newPasswordInput)
        val loginButton: Button = findViewById(R.id.loginButton)

        // Set up button click listener
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val oldPassword = oldPasswordInput.text.toString().trim()
            val newPassword = newPasswordInput.text.toString().trim()

            // Validate inputs
            if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else if (newPassword.length < 6) {
                Toast.makeText(this, "New password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            } else {
                // Simulate password update success
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                // Add navigation or additional logic here if needed
            }
        }
    }
}