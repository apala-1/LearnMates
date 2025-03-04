package com.example.learnmates.repository

interface RegistrationRepository {
    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit)
}