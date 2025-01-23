package com.example.learnmates.model

import android.net.Uri


data class Post(
    val text: String,
    val imageUri: Uri? = null
)
