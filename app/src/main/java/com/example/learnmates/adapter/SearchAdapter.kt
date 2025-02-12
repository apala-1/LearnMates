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

class SearchAdapter(val context: Context, var data: ArrayList<UserModel>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uName: TextView = itemView.findViewById(R.id.searchedName)
        val addBtn: Button = itemView.findViewById(R.id.addBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = data[position]
        holder.uName.text = user.fullname ?: user.username

        holder.addBtn.setOnClickListener {
            // Get current user's UID (Sneha's UID)
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

            if (currentUserId != null && user.userId != currentUserId) {
                // Get Firebase database reference
                val database = FirebaseDatabase.getInstance().getReference("users")

                // Apala's UID (friend) is being added to Sneha's friend list
                val friendId = user.userId
                val friendRef = database.child(currentUserId).child("friends").child(friendId)

                // Add friend (Apala) to Sneha's friend list
                friendRef.setValue(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Remove user from the search list (Apala will no longer appear)
                        data.removeAt(position) // Remove the friend from the list
                        notifyDataSetChanged() // Update the RecyclerView

                        // Optionally, add Sneha's UID to Apala's friend list (reciprocal friendship)
                        val userFriendRef = database.child(friendId).child("friends").child(currentUserId)
                        userFriendRef.setValue(true).addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                // Successfully updated Apala's friend list
                                Toast.makeText(context, "${user.fullname} added to your friends!", Toast.LENGTH_SHORT).show()
                            } else {
                                // Failed to add Sneha to Apala's friend list
                                Toast.makeText(context, "Failed to add ${user.fullname} to friend list.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Display failure message using Toast
                        Toast.makeText(context, "Failed to add ${user.fullname} as friend.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
