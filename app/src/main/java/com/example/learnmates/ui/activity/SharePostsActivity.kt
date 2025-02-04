package com.example.learnmates.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivitySharePostsBinding
import com.example.learnmates.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File

class SharePostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySharePostsBinding
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var postsRef: DatabaseReference
    val config = mapOf(
        "cloud_name" to "dpylgmszd", // Your cloud name
        "api_key" to "728297272424484", // Your API key
        "api_secret" to "QzRimcRGelu9-CAKd_jerDRWkwc" // Your API secret
    )

    val cloudinary = Cloudinary(config)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance()
        postsRef = database.getReference("posts") // Now postsRef is properly initialized

        binding.uploadPhotoButton.setOnClickListener {
            openGallery()
        }

        binding.postButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                userRef.child("fullname").get().addOnSuccessListener { snapshot ->
                    val postMemberName = snapshot.getValue(String::class.java) ?: "Name"
                    val userName = currentUser.email?.split("@")?.get(0) ?: "username"
                    val postText = binding.postEditText.text.toString()
                    val likeCount = 0
                    val savePost = false
                    val postId = postsRef.push().key

                    if (selectedImageUri != null) {
                        uploadImageToCloudinary(selectedImageUri!!, onSuccess = { imageUrl ->
                            savePostToFirebase(postId, postMemberName, userName, postText, imageUrl, likeCount, savePost)
                        }, onFailure = { error ->
                            Toast.makeText(this, "Upload Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                        })
                    } else {
                        savePostToFirebase(postId, postMemberName, userName, postText, "", likeCount, savePost)
                    }
                }.addOnFailureListener { e ->
                    Log.e("Post", "Failed to get user fullname", e)
                }
            } else {
                Log.e("Post", "User is not logged in!")
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) it.getString(columnIndex) else uri.path ?: ""
            } else {
                uri.path ?: ""
            }
        } ?: uri.path ?: ""
    }

    private fun savePostToFirebase(
        postId: String?,
        postMemberName: String,
        userName: String,
        postText: String,
        imageUrl: String,  // Ensure this parameter is passed correctly
        likeCount: Int,
        savePost: Boolean
    ) {
        val post = PostModel(postId ?: "", postMemberName, userName, postText, imageUrl, likeCount, savePost)

        postId?.let {
            postsRef.child(it).setValue(post)
                .addOnSuccessListener {
                    Log.d("Post", "Post added successfully with image!")
                }
                .addOnFailureListener { e ->
                    Log.e("Post", "Error adding post", e)
                }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.selectedImageView.setImageURI(uri)
            }
        }
    }



    fun uploadImageToCloudinary(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val inputStream = contentResolver.openInputStream(uri)

        if (inputStream != null) {
            Thread {
                try {
                    val uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap())
                    val imageUrl = uploadResult["secure_url"] as String
                    Log.d("Cloudinary", "Image URL: $imageUrl")  // Log image URL here
                    runOnUiThread { onSuccess(imageUrl) }
                } catch (e: Exception) {
                    runOnUiThread { onFailure(e) }
                }
            }.start()
        } else {
            onFailure(Exception("Failed to open input stream"))
        }
    }





}
