package com.example.learnmates.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.learnmates.R
import com.example.learnmates.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var profileImageView: ImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var saveButton: Button
    private var selectedImageUri: Uri? = null
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.profileImageView)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        saveButton = findViewById(R.id.saveButton)

        loadUserProfile()

        profileImageView.setOnClickListener {
            pickImageFromGallery()
        }

        saveButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun loadUserProfile() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("fullname").getValue(String::class.java) ?: ""
                    val username = snapshot.child("username").getValue(String::class.java) ?: ""
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""

                    fullNameEditText.setText(fullName)
                    usernameEditText.setText(username)

                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this@EditProfileActivity)
                            .load(profileImageUrl)
                            .into(profileImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            profileImageView.setImageURI(selectedImageUri)
        }
    }

    private fun updateProfile() {
        val fullName = fullNameEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()

        if (fullName.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Full name and username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare the data map for update
        val data = mutableMapOf<String, Any>(
            "fullname" to fullName,
            "username" to username
        )

        // Check if a profile image was selected
        if (selectedImageUri != null) {
            // Upload image to Cloudinary and get the URL
            uploadImageToCloudinary(selectedImageUri!!) { imageUrl ->
                if (!imageUrl.isNullOrEmpty()) {
                    data["profileImageUrl"] = imageUrl
                }
                // Update the user profile data in Firebase
                val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                databaseRef.updateChildren(data)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                            finish() // Close the activity
                        } else {
                            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            // If no profile image, just update the text fields
            val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseRef.updateChildren(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun uploadImageToCloudinary(imageUri: Uri, callback: (String?) -> Unit) {
        val cloudinary = Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dpylgmszd",  // Replace with your Cloudinary cloud name
            "api_key", "728297272424484", // Replace with your Cloudinary API key
            "api_secret", "QzRimcRGelu9-CAKd_jerDRWkwc" // Replace with your Cloudinary API secret
        ))

        val inputStream = contentResolver.openInputStream(imageUri)
        val file = File(cacheDir, "temp_image.jpg")
        file.outputStream().use { output ->
            inputStream?.copyTo(output)
        }

        val thread = Thread {
            try {
                val response = cloudinary.uploader().upload(file.path, ObjectUtils.emptyMap())
                val imageUrl = response["secure_url"] as String
                runOnUiThread { callback(imageUrl) }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { callback(null) }
            }
        }
        thread.start()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}
