package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MessageActivity : AppCompatActivity() {

    private lateinit var messagesDatabase: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var currentUserId: String
    private lateinit var friendsList: LinearLayout // Dynamically add friends here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        messagesDatabase = FirebaseDatabase.getInstance().getReference("messages")
        userDatabase = FirebaseDatabase.getInstance().getReference("users") // For user details

        friendsList = findViewById(R.id.friendsListContainer) // Add a container in the XML to hold friends

        val findMoreBuddiesButton: Button = findViewById(R.id.findMoreBuddies)
        findMoreBuddiesButton.setOnClickListener {
            val intent = Intent(this, FindBuddiesActivity::class.java)
            startActivity(intent)
        }

        // Fetch and display accepted friends
        fetchFriends()
    }

    private fun fetchFriends() {
        messagesDatabase.child(currentUserId).child("friends").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val friendId = snapshot.key ?: return

                // Get the friend request status
                snapshot.getValue(Boolean::class.java)?.let { isFriendAccepted ->
                    if (isFriendAccepted) {
                        // Get the user's data (name, ID) from the "users" database
                        userDatabase.child(friendId).get().addOnSuccessListener { dataSnapshot ->
                            val friendName = dataSnapshot.child("fullname").getValue(String::class.java) ?: "Unknown"
                            val friendButton = createFriendButton(friendName, friendId)
                            friendsList.addView(friendButton) // Add to container dynamically
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun createFriendButton(friendName: String, friendId: String): Button {
        val button = Button(this)
        button.text = "Chat with $friendName"
        button.setOnClickListener {
            navigateToChat(friendId, friendName)
        }
        return button
    }

    private fun navigateToChat(friendId: String, friendName: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("FRIEND_ID", friendId)
        intent.putExtra("FRIEND_NAME", friendName)
        startActivity(intent)
    }
}
