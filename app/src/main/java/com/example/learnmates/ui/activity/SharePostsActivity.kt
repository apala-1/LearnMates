package com.example.learnmates.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.example.learnmates.model.Post
import com.example.learnmates.viewmodel.PostViewModel

class SharePostsActivity : AppCompatActivity() {

    private val IMAGE_PICK_REQUEST_CODE = 101
    private var selectedImageUri: Uri? = null

    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_posts)

        findViewById<Button>(R.id.uploadPhotoButton).setOnClickListener {
            pickImage()
        }

        findViewById<Button>(R.id.postButton).setOnClickListener {
            val text = findViewById<EditText>(R.id.postEditText).text.toString()
            if (text.isNotBlank() || selectedImageUri != null) {
                val newPost = Post(text, selectedImageUri)
                postViewModel.addPost(newPost)

                val resultIntent = Intent().apply {
                    putExtra(EXTRA_POST_TEXT, text)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
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
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            findViewById<ImageView>(R.id.previewImageView).apply {
                visibility = View.VISIBLE
                setImageURI(selectedImageUri)
            }
        }
    }

    companion object {
        const val EXTRA_POST_TEXT = "extra_post_text"
    }
}
