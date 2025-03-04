package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.adapter.SearchAdapter
import com.example.learnmates.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivitySearchBinding
import com.example.learnmates.model.NotificationModel
import com.example.learnmates.ui.fragment.HomePageFragment
import com.example.learnmates.ui.fragment.ProfileFragment
import com.google.firebase.database.*
import com.google.firebase.database.ServerValue

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var database: DatabaseReference
    private lateinit var friendRequestsDatabase: DatabaseReference
    private lateinit var notificationsDatabase: DatabaseReference
    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menubtn.setOnClickListener {
            showPopupMenu(it)
        }

        searchEditText = findViewById(R.id.searchEditText)
        searchRecyclerView = findViewById(R.id.recyclerView)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()
        searchAdapter = SearchAdapter(this, userList)
        searchRecyclerView.adapter = searchAdapter

        database = FirebaseDatabase.getInstance().getReference("users")
        friendRequestsDatabase = FirebaseDatabase.getInstance().getReference("friendRequests")
        notificationsDatabase = FirebaseDatabase.getInstance().getReference("notifications")

        fetchUsers("")

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fetchUsers(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchUsers(query: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null && user.userId != currentUserId) {
                        val nameMatch = user.fullname?.contains(query, ignoreCase = true) == true
                        val usernameMatch = user.username?.contains(query, ignoreCase = true) == true
                        if (query.isEmpty() || nameMatch || usernameMatch) {
                            checkIfAlreadyRequested(currentUserId, user)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchDebug", "Error fetching users: ${error.message}")
            }
        })
    }

    private fun checkIfAlreadyRequested(currentUserId: String, user: UserModel) {
        friendRequestsDatabase.child(currentUserId).child(user.userId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    userList.add(user)
                    searchAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchDebug", "Error checking friend requests: ${error.message}")
            }
        })
    }

    // Send notification when a friend request is sent
    private fun sendFriendRequestNotification(receiverId: String, senderId: String) {
        // Assuming you are sending a friend request
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Fetch the sender's name asynchronously
        getSenderName(senderId) { senderName ->
            // Create the notification model
            val notification = NotificationModel(
                senderId = senderId,
                senderName = senderName,  // Use the fetched sender's name
                message = "You have a new friend request",
                timestamp = System.currentTimeMillis()
            )

            // Add notification to Firebase
            val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
            notificationsRef.child(receiverId).child(senderId).setValue(notification)
        }
    }

    private fun getSenderName(senderId: String, callback: (String) -> Unit) {
        val userDatabase = FirebaseDatabase.getInstance().getReference("users")

        userDatabase.child(senderId).get().addOnSuccessListener { dataSnapshot ->
            val senderName = dataSnapshot.child("fullname").getValue(String::class.java) ?: "Unknown"
            // Pass the sender's name to the callback
            callback(senderName)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this@SearchActivity, view) // Use 'this@NotificationActivity' instead of requireContext()
        popupMenu.menuInflater.inflate(R.menu.dropdown, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@SearchActivity, HomePageFragment::class.java))
                    true
                }

                R.id.profile -> {
                    startActivity(Intent(this@SearchActivity, ProfileFragment::class.java))
                    true

                }

                R.id.message -> {
                    startActivity(Intent(this@SearchActivity, MessageActivity::class.java))
                    true
                }

                R.id.search -> {
                    Toast.makeText(
                        this@SearchActivity,
                        "You are currently in the search page",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                R.id.notification -> {
                    startActivity(Intent(this@SearchActivity, NotificationActivity::class.java))
                    true
                }

                R.id.feedback -> {
                    startActivity(Intent(this@SearchActivity, FeedbackActivity::class.java))
                    true
                }

                R.id.saved -> {
                    startActivity(Intent(this@SearchActivity, SavedActivity::class.java))
                    true
                }

                R.id.helpcenter -> {
                    startActivity(Intent(this@SearchActivity, HelpCenterActivity::class.java))
                    true
                }

                R.id.settings -> {
                    startActivity(Intent(this@SearchActivity, SettingsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }



}
