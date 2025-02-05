package com.example.learnmates.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnmates.adapter.PostAdapter
import com.example.learnmates.databinding.FragmentHomePageBinding
import com.example.learnmates.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: ArrayList<PostModel>
    private val database = FirebaseDatabase.getInstance().getReference("posts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList = ArrayList()
        postAdapter = PostAdapter(postList) { post ->
            handleSavePost(post)
        }

        // Setup RecyclerView
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecyclerView.adapter = postAdapter

        fetchPostsFromFirebase()
    }

    private fun fetchPostsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(PostModel::class.java)
                    post?.let { postList.add(it) }
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load posts", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSavePost(post: PostModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userSaveRef = FirebaseDatabase.getInstance().getReference("users")
            .child(userId).child("savedPosts").child(post.postId)

        userSaveRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Remove post from saved posts (unsave it)
                userSaveRef.removeValue().addOnSuccessListener {
                    // Update the button visually (unsaved)
                    updatePostSavedState(post, false)
                    Toast.makeText(requireContext(), "Post unsaved", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Save the post to savedPosts
                userSaveRef.setValue(post).addOnSuccessListener {
                    // Update the button visually (saved)
                    updatePostSavedState(post, true)
                    Toast.makeText(requireContext(), "Post saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePostSavedState(post: PostModel, isSaved: Boolean) {
        // Update the button visually by notifying adapter that the save state has changed
        val index = postList.indexOfFirst { it.postId == post.postId }
        if (index != -1) {
            val updatedPost = postList[index].copy(savePost = isSaved)  // Correct field name here
            postList[index] = updatedPost
            postAdapter.notifyItemChanged(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
