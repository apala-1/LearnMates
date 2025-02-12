package com.example.learnmates.repository

import com.example.learnmates.model.UserModel

class SearchRepositoryImpl {
    fun searchUsers(name: String): List<UserModel> {
        // Mocked search function, replace this with actual database query
        return listOf(
            UserModel("1", "user1@example.com", "John Doe", "johndoe"),
            UserModel("2", "user2@example.com", "Jane Doe", "janedoe")
        ).filter { it.fullname.contains(name, ignoreCase = true) }
    }
}
