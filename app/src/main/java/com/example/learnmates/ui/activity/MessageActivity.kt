package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        val buddy1ChatButton: Button = findViewById(R.id.buddy1Chat)
        val buddy2ChatButton: Button = findViewById(R.id.buddy2Chat)
        val buddy3ChatButton: Button = findViewById(R.id.buddy3Chat)


        val findMoreBuddiesButton: Button = findViewById(R.id.findMoreBuddies)


        buddy1ChatButton.setOnClickListener {
            navigateToChat("Apala Lamichhane")
        }

        buddy2ChatButton.setOnClickListener {
            navigateToChat("Sneha Manandhar")
        }

        buddy3ChatButton.setOnClickListener {
            navigateToChat("Anjali Bist")
        }


        findMoreBuddiesButton.setOnClickListener {
            val intent = Intent(this, FindBuddiesActivity::class.java)
            startActivity(intent)
        }
    }


    private fun navigateToChat(buddyName: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("BUDDY_NAME", buddyName)
        startActivity(intent)
    }
}