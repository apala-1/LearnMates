package com.example.learnmates.ui.activity


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivitySharePostsBinding
import com.example.learnmates.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File


class SharePostsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySharePostsBinding
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    fun openGallery(){
        val intent = Intent(Intent.
        ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    val config = mapOf(
        "cloud_name" to "dpylgmszd",
        "api_key" to "728297272424484",
        "api_secret" to "QzRimcRGelu9-CAKd_jerDRWkwc"
    )

    val cloudinary = Cloudinary(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance()
        val postsRef = database.getReference("posts")

        binding.uploadPhotoButton.setOnClickListener {
            openGallery()
        }

        binding.postButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser

            if(currentUser != null){

                val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
                userRef.child("fullname").get().addOnSuccessListener { snapshot ->
                    val postMemberName = snapshot.getValue(String::class.java) ?: "Name"
                    Log.d("Post", "Full name: $postMemberName")

                    val userName = currentUser.email?.split("@")?.get(0)?: "username"
                    val postText = binding.postEditText.text.toString()
                    val likeCount = 0
                    val savePost = false

                    val postId = postsRef.push().key

                    val post = PostModel(postId?: "",postMemberName, userName, postText, "", likeCount, savePost)

                    postId?.let {
                        postsRef.child(it).setValue(post).addOnSuccessListener {
                            Log.d("Post", "Post added successfully!")
                        }
                            .addOnFailureListener { e->
                                Log.e("Post", "Error adding post", e)
                            }
                    }
                }.addOnFailureListener {e->
                    Log.e("Post", "Failed to get user fullname", e)
                }
            }
                else{
                Log.e("Post", "User is not logged in!")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!= null){
            selectedImageUri = data.data
            binding.selectedImageView.setImageURI(selectedImageUri)
        }
    }

    fun uploadImageToCloudinary(imagePath: String, onSuccess: (String)-> Unit, onFailure: (Exception) -> Unit){
        Thread{
            try {
                val uploadResult = cloudinary.uploader().upload(File(imagePath), ObjectUtils.emptyMap())
                val imageUrl = uploadResult["secure_url"] as String
                runOnUiThread { onSuccess(imageUrl) }
            }catch (e:Exception){
                runOnUiThread { onFailure(e) }
            }
        }.start()
    }
}
