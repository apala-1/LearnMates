package com.example.learnmates.ui.activity

import com.example.learnmates.ui.fragment.ProfileFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivityHomeBinding
import com.example.learnmates.ui.fragment.HomePageFragment
import androidx.activity.viewModels

class HomeActivity : AppCompatActivity() {


    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomePageFragment())

        binding.bottomView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.navHome -> replaceFragment(HomePageFragment())
                R.id.navProfile -> replaceFragment(ProfileFragment())
                else -> { }
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager =
            supportFragmentManager

        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }
}