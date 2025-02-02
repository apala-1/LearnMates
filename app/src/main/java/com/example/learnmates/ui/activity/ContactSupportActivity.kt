package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.databinding.ActivityContactSupportBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ContactSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactSupportBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // View Binding
        binding = ActivityContactSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("SupportMessages")

        // Handle Submit Button Click
        binding.sendButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val complaint = binding.complaint.text.toString().trim()

            if (validateForm(name, email, complaint)) {
                sendSupportMessage(name, email, complaint)
            }
        }

        // Handle UI Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Validate form inputs
    private fun validateForm(name: String, email: String, complaint: String): Boolean {
        var isValid = true
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (name.isEmpty()) {
            binding.name.error = "Name is required"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.email.error = "Email is required"
            isValid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.email.error = "Enter a valid email address"
            isValid = false
        }

        if (complaint.isEmpty()) {
            binding.complaint.error = "Complaint is required"
            isValid = false
        } else if (complaint.length < 10) {
            binding.complaint.error = "Complaint must be at least 10 characters long"
            isValid = false
        }

        return isValid
    }

    // Send support message to Firebase Realtime Database
    private fun sendSupportMessage(name: String, email: String, complaint: String) {
        val supportId = database.push().key // Generate unique ID
        if (supportId != null) {
            val supportMessage = SupportMessage(supportId, name, email, complaint, System.currentTimeMillis())

            database.child(supportId).setValue(supportMessage).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show()
                    binding.name.text?.clear()
                    binding.email.text?.clear()
                    binding.complaint.text?.clear()
                } else {
                    Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Data class for support message
    data class SupportMessage(val id: String, val name: String, val email: String, val complaint: String, val timestamp: Long)
}
