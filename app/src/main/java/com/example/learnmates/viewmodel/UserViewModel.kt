package com.example.learnmates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnmates.model.UserModel
import com.example.learnmates.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    // LiveData for user data
    private val _userData = MutableLiveData<UserModel?>()
    val userData: LiveData<UserModel?> = _userData

    // Login function
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    // Signup function
    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signup(email, password, callback)
    }

    // Forgot password function
    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgotPassword(email, callback)
    }

    // Add user to database
    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    // Fetch user from database
    fun getUserFromDatabase(userId: String) {
        repo.getUserFromDatabase(userId) { userModel, success, message ->
            if (success) {
                _userData.value = userModel
            } else {
                _userData.value = null
            }
        }
    }

    // Logout function
    fun logout(callback: (Boolean, String) -> Unit) {
        repo.logout(callback)
    }

    // Edit profile function
    fun editProfile(userId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.editProfile(userId, data, callback)
    }
}
