package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class FrontPageActivity : AppCompatActivity() {
    lateinit var signupbutton : Button
    lateinit var loginbutton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.example.learnmates.R.layout.activity_front_page)

        signupbutton = findViewById(com.example.learnmates.R.id.signUpBtn)
        loginbutton = findViewById(com.example.learnmates.R.id.loginBtn)

        signupbutton.setOnClickListener { v: View? ->
            val intent: Intent =
                Intent(
                    this@FrontPageActivity,
                    SignupActivity::class.java
                )
            startActivity(intent)
        }

        loginbutton.setOnClickListener { v: View? ->
            val intent: Intent =
                Intent(
                    this@FrontPageActivity,
                    LogInActivity::class.java
                )
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.learnmates.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}