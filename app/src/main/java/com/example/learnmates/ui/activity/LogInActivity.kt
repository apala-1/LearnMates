package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivityLogInBinding
import com.example.learnmates.utils.LoadingUtils
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding
    lateinit var forgotPassword : TextView
    lateinit var loadingUtils: LoadingUtils
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtils = LoadingUtils(this)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener {
            val email  =binding.editTextEmail.text.toString()
            val password  =binding.editTextPassword.text.toString()
            if(email.isNotEmpty()&&password.isNotEmpty()){
                loadingUtils.show()
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    loadingUtils.dismiss()
                    if (task.isSuccessful){
                        Toast.makeText(
                            this@LogInActivity,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    }else{
                        Toast.makeText(
                            this@LogInActivity,
                            "Error:  ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(
                    this@LogInActivity,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        forgotPassword = findViewById(R.id.forgotPasswordLink)
        forgotPassword.setOnClickListener{ v: View? ->
            val intent: Intent =
                Intent(
                    this@LogInActivity,
                    ForgotPassActivity::class.java
                )
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun navigateToHome(){
        val intent = Intent(
            this@LogInActivity,
            HomeActivity::class.java
        )
        startActivity(intent)
    }
}