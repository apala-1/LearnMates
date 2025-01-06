package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gettingStarted.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, GettingStartedActivity::class.java)
            startActivity(intent)
        }

        binding.faqs.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, FAQsActivity::class.java)
            startActivity(intent)
        }

        binding.contactSupport.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, ContactSupportActivity::class.java)
            startActivity(intent)
        }

        binding.privacyandsecurity.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, PrivacyAndSecurityActivity::class.java)
            startActivity(intent)
        }

        binding.communityguidelines.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, CommunityGuidelinesActivity::class.java)
            startActivity(intent)
        }

        binding.reportabuse.setOnClickListener {
            val intent = Intent(this@HelpCenterActivity, ReportAbuseActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
