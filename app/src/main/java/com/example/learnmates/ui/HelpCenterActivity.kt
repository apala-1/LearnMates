package com.example.learnmates.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import androidx.appcompat.widget.SearchView

class HelpCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help_center)

        var gettingStarted = findViewById<TextView>(R.id.gettingStarted)
        var FAQs = findViewById<TextView>(R.id.faqs)
        var contactSupport = findViewById<TextView>(R.id.contactSupport)
        var PrivacyAndSecurity = findViewById<TextView>(R.id.privacyandsecurity)
        var CommunityGuildelines = findViewById<TextView>(R.id.communityguidelines)
        var reportAbuse = findViewById<TextView>(R.id.reportabuse)

        gettingStarted.setOnClickListener{

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}