package com.example.learnmates.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.learnmates.R
import com.example.learnmates.databinding.FragmentProfileBinding
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.example.learnmates.repository.UserRepositoryImpl
import com.example.learnmates.ui.activity.FeedbackActivity
import com.example.learnmates.ui.activity.HelpCenterActivity
import com.example.learnmates.ui.activity.HomeActivity
import com.example.learnmates.ui.activity.MessageActivity
import com.example.learnmates.ui.activity.NotificationActivity
import com.example.learnmates.ui.activity.SavedActivity
import com.example.learnmates.ui.activity.SearchActivity
import com.example.learnmates.ui.activity.SettingsActivity
import com.example.learnmates.ui.activity.SharePostsActivity
import com.example.learnmates.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    lateinit var userViewModel: UserViewModel
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

        loadUserProfile()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }


    private fun loadUserProfile() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("fullname").getValue(String::class.java) ?: ""
                    val username = snapshot.child("username").getValue(String::class.java) ?: ""
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""

                    // Update the UI with the new data
                    binding.profileName.text = fullName
                    binding.username.text = username
                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(requireContext()).load(profileImageUrl).into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        var currentUser = userViewModel.getCurrentUser()

        currentUser.let {
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }

        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Update profile name and username
                binding.profileName.text = it.fullname
                binding.username.text = it.username

                // Update profile image if available
                val profileImageUrl = it.profileImageUrl
                if (!profileImageUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(profileImageUrl) // Load the image from the URL
                        .placeholder(R.drawable.baseline_fireplace_24) // Optional placeholder while loading
                        .into(binding.profileImage) // Update the ImageView with the new image
                }
            }
        }


        binding.SharePosts.setOnClickListener {
            startActivity(Intent(requireContext(), SharePostsActivity::class.java))
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
            Toast.makeText(context, "You can now edit your About Me section.", Toast.LENGTH_SHORT)
                .show()
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

        binding.additionalIcon.setOnClickListener {
            showPopupMenu(it)
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
                    Toast.makeText(
                        context,
                        "You are currently in the profile page",
                        Toast.LENGTH_SHORT
                    ).show()
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

                R.id.settings -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}
