package com.example.learnmates.repository

import com.example.learnmates.model.UserModel

interface SearchRepository {
    fun getUsersByName(query: String, callback: (List<UserModel>) -> Unit)
}
