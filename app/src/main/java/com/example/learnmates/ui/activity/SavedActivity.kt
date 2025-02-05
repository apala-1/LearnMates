package com.example.learnmates.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.adapter.PostAdapter
import com.example.learnmates.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SavedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var savedPostAdapter: PostAdapter
    var savedPostsList = mutableListOf<PostModel>()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        recyclerView = findViewById(R.id.savedPostsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with the save action callback
        savedPostAdapter = PostAdapter(savedPostsList) { post ->
            handleSavePost(post)
        }
        recyclerView.adapter = savedPostAdapter

        loadSavedPosts()

        // Handling insets for proper layout behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedPosts()
    }

    private fun loadSavedPosts() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userSaveRef = database.child(userId).child("savedPosts")

        userSaveRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedPostsList.clear()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(PostModel::class.java)
                    Log.d("SavedActivity", "Post Retrieved: ${post?.postText}")
                    post?.let { savedPostsList.add(it) }
                }

                Log.d("SavedActivity", "Total saved posts: ${savedPostsList.size}")

                // 🔥 Set adapter again to force UI refresh
                savedPostAdapter = PostAdapter(savedPostsList) { post -> handleSavePost(post) }
                recyclerView.adapter = savedPostAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SavedActivity", "Failed to load saved posts", error.toException())
            }
        })
    }



    private fun handleSavePost(post: PostModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userSaveRef = database.child(userId).child("savedPosts").child(post.postId)

        userSaveRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Post is already saved, unsave it
                userSaveRef.removeValue()
            } else {
                // Post is not saved, save it
                userSaveRef.setValue(post)
            }
            loadSavedPosts() // Reload saved posts after save/unsave action
        }
    }
}
