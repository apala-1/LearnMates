package com.example.learnmates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.model.SearchModel
import com.example.learnmates.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAdapter(val context: Context, var data: ArrayList<UserModel>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uName: TextView = itemView.findViewById((R.id.searchedName))
        val addBtn: Button = itemView.findViewById(R.id.addBtn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = data[position]
        holder.uName.text = user.fullname ?: user.username // ✅ Use correct property

        holder.addBtn.setOnClickListener {
            addFriendToDatabase(user, holder)
        }


    }

    private fun addFriendToDatabase(user: UserModel, holder: SearchAdapter.SearchViewHolder) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseRef = FirebaseDatabase.getInstance().getReference("friends").child(currentUserId)
        val friendData = mapOf(
            "friendId" to user.userId,
            "friendName" to (user.fullname ?: user.username)
        )

        databaseRef.child(user.userId).setValue(friendData)
            .addOnSuccessListener {
                Toast.makeText(context, "Friend added successfully!", Toast.LENGTH_SHORT).show()
                holder.addBtn.isEnabled = false  // Disable button after adding
                holder.addBtn.text = "Added"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add friend", Toast.LENGTH_SHORT).show()
            }
    }
    }
