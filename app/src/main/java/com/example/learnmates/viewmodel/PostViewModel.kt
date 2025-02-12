package com.example.learnmates.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnmates.model.Post

class PostViewModel : ViewModel() {
    val posts: MutableLiveData<List<Post>> = MutableLiveData(emptyList())

    fun addPost(post: Post) {
        val currentPosts = posts.value.orEmpty().toMutableList()
        currentPosts.add(post)
        posts.value = currentPosts
    }
}
