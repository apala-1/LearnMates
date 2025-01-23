package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivityForgotPassBinding
import com.example.learnmates.utils.LoadingUtils
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPassBinding
    lateinit var loadingUtils: LoadingUtils
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.resetPassBtn.setOnClickListener {
            val email =  binding.etEmail.text.toString()
            if (email.isNotEmpty()){
                loadingUtils.show()
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    loadingUtils.dismiss()
                    if (task.isSuccessful){
                        Toast.makeText(
                            this@ForgotPassActivity,
                            "Reset link sent to your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            this@ForgotPassActivity,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(
                    this@ForgotPassActivity,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        backBtn = findViewById(R.id.backBtn)

        backBtn.setOnClickListener{
            val intent : Intent =
                Intent(
                    this@ForgotPassActivity,
                    FrontPageActivity::class.java
                )
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}