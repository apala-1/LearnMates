package com.example.learnmates.repository

import com.google.firebase.auth.FirebaseAuth

class RegistrationRepositoryImpl(var auth: FirebaseAuth): RegistrationRepository {
    override fun signup(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                val  userId = auth.currentUser?.uid.toString()
                callback(true, "SignUp Successful", userId)
            }else{
                callback(false,it.exception?.message.toString(), "")
            }
        }
    }
}