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
import com.example.learnmates.databinding.ActivitySignupBinding
import com.example.learnmates.model.UserModel
import com.example.learnmates.repository.UserRepositoryImpl
import com.example.learnmates.utils.LoadingUtils
import com.example.learnmates.viewmodel.UserViewModel

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var userViewModel: UserViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var accountBtn : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)

        val userRepository = UserRepositoryImpl()

        userViewModel = UserViewModel(userRepository)

        binding.signUpBtn.setOnClickListener {
            loadingUtils.show()
            var email: String = binding.editTextEmail.text.toString()
            var password: String = binding.editTextPassword.text.toString()
            var fullname: String = binding.editTextFullName.text.toString()
            var username: String = binding.editTextUsername.text.toString()
            userViewModel.signup(email,password){
                success, message, userId ->
                if (success){
                    var userModel = UserModel(
                        userId, email, fullname, username
                    )
                    addUser(userModel)
                }else{
                    loadingUtils.dismiss()
                    Toast.makeText(this@SignupActivity,
                        message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        accountBtn = findViewById(R.id.alreadyHave)

        accountBtn.setOnClickListener { v : View? ->
            val intent : Intent =
                Intent(
                    this@SignupActivity,
                    LogInActivity::class.java
                )
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun addUser(userModel: UserModel){
        userViewModel.addUserToDatabase(userModel.userId,userModel){
                success, message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(this@SignupActivity,
                    message,Toast.LENGTH_SHORT).show()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(this@SignupActivity,
                    message,Toast.LENGTH_SHORT).show()
            }
        }
    }
}