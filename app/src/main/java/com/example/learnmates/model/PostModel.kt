package com.example.learnmates.model

data class PostModel(
    var postId: String = "",
    var postMemberName: String = "",
    var userName: String = "",
    var postText: String = "",
    var postImage: String = "",
    var likeCount: Int = 0,
    var savePost: Boolean = false
) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "", "", 0, false)
}
