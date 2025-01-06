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

class FeedbackActivity : AppCompatActivity() {
    lateinit var emailAddress: TextInputEditText
    lateinit var feedbackText: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedback)

        emailAddress = findViewById(R.id.email)
        feedbackText = findViewById(R.id.text)
        var submitButton = findViewById<AppCompatButton>(R.id.submitButton)

        submitButton.setOnClickListener{
            if(validateForm(emailAddress, feedbackText)){
                    Toast.makeText(this, "Form is valid!", Toast.LENGTH_SHORT).show()
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
            if(emailFeed.text.toString().trim().isEmpty()){
                emailFeed.error = "Email is required"
                isValid = false
            }else if (!emailFeed.text.toString().matches(emailPattern.toRegex())) {
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
}