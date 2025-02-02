package com.example.learnmates.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class SharePostsActivity : AppCompatActivity() {

    private val IMAGE_PICK_REQUEST_CODE = 101
    private var selectedImageUri: Uri? = null
    private var selectedImageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_posts)

        findViewById<Button>(R.id.uploadPhotoButton).setOnClickListener {
            pickImage()
        }

        findViewById<Button>(R.id.postButton).setOnClickListener {
            val text = findViewById<EditText>(R.id.postEditText).text.toString()
            if (text.isNotBlank() || selectedImageBase64 != null) {
                savePostToFirebase(text, selectedImageBase64)
            } else {
                Toast.makeText(this, "Enter text or select an image to post!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    @Deprecated("Deprecated in favor of Activity Result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                findViewById<ImageView>(R.id.previewImageView).apply {
                    visibility = View.VISIBLE
                    setImageURI(it)
                }
                selectedImageBase64 = uriToBase64(it)
            }
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun savePostToFirebase(text: String, imageBase64: String?) {
        val database = FirebaseDatabase.getInstance().reference.child("posts")
        val postId = database.push().key // Generate unique ID for the post

        val postMap = mapOf(
            "text" to text,
            "imageBase64" to imageBase64,
            "timestamp" to System.currentTimeMillis(),
            "userId" to FirebaseAuth.getInstance().currentUser?.uid
        )

        postId?.let {
            database.child(it).setValue(postMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to upload post: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    companion object {
        const val EXTRA_POST_TEXT = "com.example.learnmates.EXTRA_POST_TEXT"
        const val EXTRA_POST_IMAGE_URI = "com.example.learnmates.EXTRA_POST_IMAGE_URI"
    }

}
