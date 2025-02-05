package com.example.learnmates

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.adapter.SearchAdapter
import com.example.learnmates.model.UserModel
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        searchRecyclerView = findViewById(R.id.recyclerView)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()
        searchAdapter = SearchAdapter(this, userList)
        searchRecyclerView.adapter = searchAdapter

        database = FirebaseDatabase.getInstance().getReference("users")

        // Load all users initially
        fetchUsers("")

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fetchUsers(s.toString().trim()) // Fetch users based on search input
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUsers(query: String) {
        Log.d("SearchDebug", "Fetching users for query: $query")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    Log.d("SearchDebug", "Fetched user: ${user?.fullname} - ${user?.username}")

                    if (user != null) {
                        val nameMatch = user.fullname?.contains(query, ignoreCase = true) == true
                        val usernameMatch = user.username?.contains(query, ignoreCase = true) == true

                        if (query.isEmpty() || nameMatch || usernameMatch) {
                            userList.add(user)
                        }
                    }
                }
                Log.d("SearchDebug", "Final user list size: ${userList.size}")
                searchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchDebug", "Error fetching users: ${error.message}")
            }
        })
    }

}
