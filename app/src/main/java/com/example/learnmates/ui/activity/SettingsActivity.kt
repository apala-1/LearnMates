package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivitySettingsBinding
import com.example.learnmates.repository.UserRepository
import com.example.learnmates.repository.UserRepositoryImpl

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepositoryImpl()

        binding.logOutBtn.setOnClickListener {
            userRepository.logout{ success, message ->
                if (success){
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Logout Failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.editProfBtn.setOnClickListener {
            val intent = Intent(this@SettingsActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun navigateToHome(){
        val intent = Intent(this, FrontPageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or   Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}