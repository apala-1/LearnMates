package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R

class ChatActivity : AppCompatActivity() {

    private lateinit var chatDisplay: TextView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        chatDisplay = findViewById(R.id.chatDisplay)
        chatInput = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.sendButton)


        val buddyName = intent.getStringExtra("BUDDY_NAME")
        title = "Chat with $buddyName"


        sendButton.setOnClickListener {
            val message = chatInput.text.toString()
            if (message.isNotEmpty()) {
                appendMessage("You: $message")
                chatInput.text.clear()
            }
        }
    }


    private fun appendMessage(message: String) {
        val currentText = chatDisplay.text.toString()
        chatDisplay.text = "$currentText\n$message"
    }
}