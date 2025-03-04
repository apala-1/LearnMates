package com.example.learnmates.repository

interface LoginRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
}