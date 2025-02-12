package com.example.learnmates.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.learnmates.model.UserModel
import com.example.learnmates.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signup(email, password, callback)
    }

    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgotPassword(email, callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    fun getUserFromDatabase(userId: String) {
            if (success) {
                _userData.value = userModel
            } else {
                _userData.value = null
            }
        }
    }

    fun logout(callback: (Boolean, String) -> Unit) {
        repo.logout(callback)
    }

        repo.editProfile(userId, data, callback)
    }
}