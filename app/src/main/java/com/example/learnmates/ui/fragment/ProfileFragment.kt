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
import com.example.learnmates.databinding.FragmentProfileBinding
import com.example.learnmates.model.Post
import com.example.learnmates.repository.UserRepositoryImpl
import com.example.learnmates.ui.activity.*
import com.example.learnmates.viewmodel.PostViewModel
import com.example.learnmates.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    private lateinit var postAdapter: PostAdapter
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        postAdapter = PostAdapter(mutableListOf())
        binding.recyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }

        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }

        binding.SharePosts.setOnClickListener {
            val intent = Intent(requireContext(), SharePostsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SHARE_POST)
        }

        binding.aboutMeButton.setOnClickListener {
            binding.aboutMeSection.visibility = View.VISIBLE
        }

        binding.editButton.setOnClickListener {
            binding.aboutMeText.apply {
                isFocusableInTouchMode = true
                isFocusable = true
                requestFocus()
            }
            Toast.makeText(context, "You can now edit your About Me section.", Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            binding.aboutMeText.apply {
                isFocusableInTouchMode = false
                isFocusable = false
            }
            val updatedText = binding.aboutMeText.text.toString()
            Toast.makeText(context, "About Me saved: $updatedText", Toast.LENGTH_SHORT).show()
            binding.aboutMeSection.visibility = View.GONE
        }

        binding.additionalIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val currentUser = userViewModel.getCurrentUser()
        currentUser?.uid?.let { userId ->
            userViewModel.getUserFromDatabase(userId)
        }

        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            binding.profileName.text = user?.fullname
            binding.username.text = user?.username
        }
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