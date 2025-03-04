package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatDisplay: TextView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesDatabase: DatabaseReference
    private lateinit var currentUserId: String
    private lateinit var chatId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatDisplay = findViewById(R.id.chatDisplay)
        chatInput = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.sendButton)

        val buddyId = intent.getStringExtra("FRIEND_ID") ?: ""
        val buddyName = intent.getStringExtra("FRIEND_NAME") ?: ""
        title = "Chat with $buddyName"

        currentUserId = FirebaseAuth.getInstance().currentUser ?.uid ?: ""
        chatId = if (currentUserId < buddyId) {
            "$currentUserId-$buddyId"
        } else {
            "$buddyId-$currentUserId"
        }

        messagesDatabase = FirebaseDatabase.getInstance().getReference("messages").child(chatId)

        // Listen for new messages
        listenForMessages()

        sendButton.setOnClickListener {
            val message = chatInput.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
                chatInput.text.clear()
            }
        }
    }

    private fun sendMessage(message: String) {
        val messageId = messagesDatabase.push().key ?: return
        val messageData = mapOf(
            "senderId" to currentUserId,
            "receiverId" to intent.getStringExtra("FRIEND_ID"),
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )
        messagesDatabase.child(messageId).setValue(messageData)
    }

    private fun listenForMessages() {
        messagesDatabase.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    // Fetch the sender's name from the database
                    fetchSenderName(it.senderId) { senderName ->
                        appendMessage("$senderName: ${it.message}")
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchSenderName(senderId: String, callback: (String) -> Unit) {
        val userDatabase = FirebaseDatabase.getInstance().getReference("users")
        userDatabase.child(senderId).child("fullname").get().addOnSuccessListener { dataSnapshot ->
            val senderName = dataSnapshot.getValue(String::class.java) ?: "Unknown"
            callback(senderName)
        }.addOnFailureListener {
            callback("Unknown")
        }
    }

    private fun appendMessage(message: String) {
        val currentText = chatDisplay.text.toString()
        chatDisplay.text = "$currentText\n$message"
    }

    data class Message(
        val senderId: String = "",
        val receiverId: String = "",
        val message: String = "",
        val timestamp: Long = 0
    )
}