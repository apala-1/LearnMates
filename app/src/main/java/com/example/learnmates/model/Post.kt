package com.example.learnmates.model

import android.net.Uri

// Post data class

data class Post(
    val text: String,
    val imageUri: Uri? = null
)
