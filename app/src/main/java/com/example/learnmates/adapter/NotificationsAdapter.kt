package com.example.learnmates.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotificationsAdapter(
    private val notificationsList: MutableList<NotificationModel>,
    private val currentUserId: String
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private val friendRequestsDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("friendRequests")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notificationsList[position]
        holder.messageTextView.text = "${notification.senderName} sent you a friend request - ${notification.timestamp}"

        // Handle accept button click
        holder.acceptButton.setOnClickListener {
            acceptFriendRequest(notification.senderId, holder.itemView.context)
        }

        // Handle reject button click
        holder.rejectButton.setOnClickListener {
            rejectFriendRequest(notification.senderId, holder.itemView.context) // Pass position
        }
    }


    override fun getItemCount(): Int {
        return notificationsList.size
    }

    // When a user accepts a friend request
    private fun acceptFriendRequest(senderId: String, context: Context) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Update friendRequests node to mark the request as accepted
        val friendRequestRef = FirebaseDatabase.getInstance().getReference("friendRequests")
        friendRequestRef.child(currentUserId).child(senderId).setValue(true)
        friendRequestRef.child(senderId).child(currentUserId).setValue(true)

        // Add both users as friends in the messages node
        val messagesRef = FirebaseDatabase.getInstance().getReference("messages")
        messagesRef.child(currentUserId).child("friends").child(senderId).setValue(true)
        messagesRef.child(senderId).child("friends").child(currentUserId).setValue(true)

        // Delete the notification after accepting the friend request
        val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")

        // Fetch notifications for the current user to find the sender's notification
        notificationsRef.child(currentUserId).orderByChild("senderId").equalTo(senderId).get()
            .addOnSuccessListener { snapshot ->
                for (notificationSnapshot in snapshot.children) {
                    // Remove the notification with the matching senderId
                    notificationSnapshot.ref.removeValue()
                        .addOnSuccessListener {
                            Log.d("Notification", "Notification successfully removed for user $currentUserId and sender $senderId")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Notification", "Failed to remove notification: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Notification", "Failed to fetch notifications: ${exception.message}")
            }

        // Notify that the request was accepted
        Toast.makeText(context, "Friend request accepted", Toast.LENGTH_SHORT).show()
    }

    private fun rejectFriendRequest(senderId: String, context: Context) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Delete the friend request from the database
        val friendRequestRef = FirebaseDatabase.getInstance().getReference("friendRequests")
        friendRequestRef.child(currentUserId).child(senderId).removeValue()
        friendRequestRef.child(senderId).child(currentUserId).removeValue()

        // Delete the notification after rejecting the friend request
        val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")

        // Fetch notifications for the current user to find the sender's notification
        notificationsRef.child(currentUserId).orderByChild("senderId").equalTo(senderId).get()
            .addOnSuccessListener { snapshot ->
                for (notificationSnapshot in snapshot.children) {
                    // Remove the notification with the matching senderId
                    notificationSnapshot.ref.removeValue()
                        .addOnSuccessListener {
                            Log.d("Notification", "Notification successfully removed for user $currentUserId and sender $senderId")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Notification", "Failed to remove notification: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Notification", "Failed to fetch notifications: ${exception.message}")
            }

        // Notify that the request was rejected
        Toast.makeText(context, "Friend request rejected", Toast.LENGTH_SHORT).show()
    }



    // Helper function to remove notification from UI
    private fun removeNotification(position: Int) {
        (notificationsList as MutableList).removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val rejectButton: Button = itemView.findViewById(R.id.rejectButton)
    }

    fun updateNotificationsList(newNotificationsList: List<NotificationModel>) {
        notificationsList.clear()
        notificationsList.addAll(newNotificationsList)
        notifyDataSetChanged()
    }

}
