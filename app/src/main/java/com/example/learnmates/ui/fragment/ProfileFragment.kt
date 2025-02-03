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
import com.example.learnmates.ui.activity.FeedbackActivity
import com.example.learnmates.ui.activity.HelpCenterActivity
import com.example.learnmates.ui.activity.HomeActivity
import com.example.learnmates.ui.activity.MessageActivity
import com.example.learnmates.ui.activity.NotificationActivity
import com.example.learnmates.ui.activity.SavedActivity
import com.example.learnmates.ui.activity.SearchActivity
import com.example.learnmates.ui.activity.SettingsActivity
import com.example.learnmates.ui.activity.SharePostsActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
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
