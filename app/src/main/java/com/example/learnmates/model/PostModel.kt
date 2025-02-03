package com.example.learnmates.model

import android.net.Uri

data class PostModel (
    var postId: String = "",
    var postMemberName: String = "",
    var userName: String = "",
    var postText: String = "",
    var postImage: String = "",
    var likeCount: Int,
    var savePost: Boolean = false
){

}