package com.example.learnmates.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R

class LogInActivity : AppCompatActivity() {
    lateinit var forgotPassword : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        forgotPassword = findViewById(R.id.forgotPasswordLink)
        forgotPassword.setOnClickListener{ v: View? ->
            val intent: Intent =
                Intent(
                    this@LogInActivity,
                    ForgotPasswordActivity::class.java
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