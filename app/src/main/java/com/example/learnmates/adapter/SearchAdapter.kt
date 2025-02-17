package com.example.learnmates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SearchAdapter(private val context: Context, private val userList: ArrayList<UserModel>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val friendRequestsDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("friendRequests")
    private val currentUserId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val usersDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.usernameTextView.text = user.username
        holder.fullnameTextView.text = user.fullname

        holder.addFriendButton.setOnClickListener {
            sendFriendRequest(user, holder)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun sendFriendRequest(user: UserModel, holder: ViewHolder) {
        if (user.userId == null) return

        val requestRef = friendRequestsDatabase.child(user.userId).child(currentUserId)
        requestRef.setValue(true)
            .addOnSuccessListener {
                friendRequestsDatabase.child(currentUserId).child(user.userId).setValue(false)
                holder.addFriendButton.isEnabled = false
                holder.addFriendButton.text = "Request Sent"

                // Get the current user's name (sender's name)
                val senderNameRef = usersDatabase.child(currentUserId).child("fullname")
                senderNameRef.get().addOnSuccessListener { snapshot ->
                    val senderName = snapshot.getValue(String::class.java) ?: "Unknown User"

                    // Store notification in Firebase with sender's name
                    val notificationRef = FirebaseDatabase.getInstance().getReference("notifications").child(user.userId)
                    val notificationId = notificationRef.push().key

                    val notificationData = hashMapOf(
                        "senderId" to currentUserId,
                        "senderName" to senderName, // Include sender's name here
                        "message" to "sent you a friend request",
                        "timestamp" to System.currentTimeMillis()
                    )

                    notificationId?.let {
                        notificationRef.child(it).setValue(notificationData)
                    }

                    Toast.makeText(context, "Friend request sent!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to send request", Toast.LENGTH_SHORT).show()
            }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.tvUserName)
        val fullnameTextView: TextView = itemView.findViewById(R.id.tvFullName)
        val addFriendButton: Button = itemView.findViewById(R.id.addFriendButton)
    }
}
