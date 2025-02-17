package com.example.learnmates.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.adapter.NotificationsAdapter
import com.example.learnmates.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ServerValue

class NotificationActivity : AppCompatActivity() {

    private lateinit var notificationsAdapter: NotificationsAdapter
    private val notificationsList = ArrayList<NotificationModel>()
    private val notificationsDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("notifications")
    private val friendsDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("friends")
    private val currentUserId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Set up RecyclerView and adapter
        notificationsAdapter = NotificationsAdapter(notificationsList, currentUserId)
        val recyclerView: RecyclerView = findViewById(R.id.notificationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notificationsAdapter

        // Set up edge-to-edge layout for the activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listen for new notifications for the current user
        listenForNotifications()
    }

    override fun onResume() {
        super.onResume()
        // Fetch and update the notifications list
        fetchNotifications()
    }

    private fun fetchNotifications() {
        val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
        notificationsRef.child(currentUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notificationsList = mutableListOf<NotificationModel>()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(NotificationModel::class.java)
                    if (notification != null) {
                        notificationsList.add(notification)
                    }
                }
                // Update RecyclerView
                notificationsAdapter.updateNotificationsList(notificationsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Notifications", "Failed to fetch notifications: ${error.message}")
            }
        })
    }

    private fun listenForNotifications() {
        notificationsDatabase.child(currentUserId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val notification = snapshot.getValue(NotificationModel::class.java)
                notification?.let {
                    notificationsList.add(it)
                    notificationsAdapter.notifyItemInserted(notificationsList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here if needed
                Toast.makeText(this@NotificationActivity, "Failed to load notifications", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Accept friend request
    private fun acceptFriendRequest(notificationId: String, senderId: String, receiverId: String) {
        // Add both users as friends
        friendsDatabase.child(senderId).child(receiverId).setValue(true)
        friendsDatabase.child(receiverId).child(senderId).setValue(true)

        // Remove the notification after acceptance
        notificationsDatabase.child(receiverId).child(notificationId).removeValue()

        // Optionally, you can show the new friend in the message activity
        Toast.makeText(this, "Friend request accepted!", Toast.LENGTH_SHORT).show()
    }

    // Reject friend request
    private fun rejectFriendRequest(notificationId: String, receiverId: String) {
        // Remove the notification after rejection
        notificationsDatabase.child(receiverId).child(notificationId).removeValue()

        Toast.makeText(this, "Friend request rejected", Toast.LENGTH_SHORT).show()
    }
}
