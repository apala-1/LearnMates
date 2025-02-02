package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FeedbackActivity : AppCompatActivity() {
    lateinit var emailAddress: TextInputEditText
    lateinit var feedbackText: TextInputEditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedback)

        emailAddress = findViewById(R.id.email)
        feedbackText = findViewById(R.id.text)
        val submitButton = findViewById<AppCompatButton>(R.id.submitButton)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("Feedback")

        submitButton.setOnClickListener {
            if (validateForm(emailAddress, feedbackText)) {
                val email = emailAddress.text.toString().trim()
                val feedback = feedbackText.text.toString().trim()

                // Save feedback to Firebase
                val feedbackId = database.push().key // Generate unique ID
                if (feedbackId != null) {
                    val feedbackData = Feedback(feedbackId, email, feedback)
                    database.child(feedbackId).setValue(feedbackData).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to submit feedback.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateForm(emailFeed: TextInputEditText?, feedbackFeed: TextInputEditText?): Boolean {
        var isValid = true

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (emailFeed != null) {
            if (emailFeed.text.toString().trim().isEmpty()) {
                emailFeed.error = "Email is required"
                isValid = false
            } else if (!emailFeed.text.toString().matches(emailPattern.toRegex())) {
                emailFeed.error = "Enter a valid email address"
                isValid = false
            }
        }

        if (feedbackFeed != null) {
            if (feedbackFeed.text.toString().trim().isEmpty()) {
                feedbackFeed.error = "Feedback is required"
                isValid = false
            } else if (feedbackFeed.text.toString().length < 10) {
                feedbackFeed.error = "Feedback must be at least 10 characters long"
                isValid = false
            }
        }

        return isValid
    }

    data class Feedback(val id: String, val email: String, val feedback: String)
}
