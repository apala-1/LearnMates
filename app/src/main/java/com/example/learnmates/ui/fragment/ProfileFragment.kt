package com.example.learnmates.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.adapter.PostAdapter
import com.example.learnmates.model.Post
import com.example.learnmates.ui.activity.*
import com.example.learnmates.viewmodel.PostViewModel

class ProfileFragment : Fragment() {

    private lateinit var aboutMeButton: Button
    private lateinit var aboutMeSection: LinearLayout
    private lateinit var aboutMeText: TextView
    private lateinit var editButton: Button
    private lateinit var saveButton: Button
    private lateinit var sharePostsButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private val postViewModel: PostViewModel by activityViewModels() // Shared ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize UI elements
        aboutMeButton = rootView.findViewById(R.id.aboutMeButton)
        aboutMeSection = rootView.findViewById(R.id.aboutMeSection)
        aboutMeText = rootView.findViewById(R.id.aboutMeText)
        editButton = rootView.findViewById(R.id.editButton)
        saveButton = rootView.findViewById(R.id.saveButton)
        sharePostsButton = rootView.findViewById(R.id.SharePosts)
        recyclerView = rootView.findViewById(R.id.recyclerView)

        // Initialize RecyclerView and Adapter
        postAdapter = PostAdapter(mutableListOf())
        recyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Observe changes to the posts in the ViewModel
        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }

        // Set button listeners
        sharePostsButton.setOnClickListener {
            val intent = Intent(requireContext(), SharePostsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SHARE_POST)
        }

        aboutMeButton.setOnClickListener {
            aboutMeSection.visibility = View.VISIBLE
        }

        editButton.setOnClickListener {
            aboutMeText.apply {
                isFocusableInTouchMode = true
                isFocusable = true
                requestFocus() // Move the cursor to the text field
            }
            Toast.makeText(context, "You can now edit your About Me section.", Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener {
            aboutMeText.apply {
                isFocusableInTouchMode = false
                isFocusable = false
            }
            val updatedText = aboutMeText.text.toString()
            Toast.makeText(context, "About Me saved: $updatedText", Toast.LENGTH_SHORT).show()
            aboutMeSection.visibility = View.GONE
        }

        // Handle dropdown menu
        rootView.findViewById<View>(R.id.additionalIcon)?.setOnClickListener { view ->
            showPopupMenu(view)
        }

        return rootView
    }

    @Deprecated("Deprecated in favor of Activity Result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SHARE_POST && resultCode == Activity.RESULT_OK) {
            val newPostText = data?.getStringExtra(SharePostsActivity.EXTRA_POST_TEXT)
            val newPostImageUri = data?.getStringExtra("extra_image_uri")?.let { Uri.parse(it) }
            Log.d("ProfileFragment", "Received image URI: $newPostImageUri")
            if (!newPostText.isNullOrBlank() || newPostImageUri != null) {
                val newPost = Post(text = newPostText.orEmpty(), imageUri = newPostImageUri)
                postViewModel.addPost(newPost)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.dropdown, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    true
                }
                R.id.profile -> {
                    Toast.makeText(context, "You are currently in the profile page", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.message -> {
                    startActivity(Intent(requireContext(), MessageActivity::class.java))
                    true
                }
                R.id.search -> {
                    startActivity(Intent(requireContext(), SearchActivity::class.java))
                    true
                }
                R.id.notification -> {
                    startActivity(Intent(requireContext(), NotificationActivity::class.java))
                    true
                }
                R.id.feedback -> {
                    startActivity(Intent(requireContext(), FeedbackActivity::class.java))
                    true
                }
                R.id.saved -> {
                    startActivity(Intent(requireContext(), SavedActivity::class.java))
                    true
                }
                R.id.helpcenter -> {
                    startActivity(Intent(requireContext(), HelpCenterActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    companion object {
        const val REQUEST_CODE_SHARE_POST = 1001
    }
}
