package com.example.learnmates.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.PopupMenu
import com.example.learnmates.R
import com.example.learnmates.ui.activity.FeedbackActivity
import com.example.learnmates.ui.activity.HelpCenterActivity
import com.example.learnmates.ui.activity.HomeActivity
import com.example.learnmates.ui.activity.MessageActivity
import com.example.learnmates.ui.activity.NotificationActivity
import com.example.learnmates.ui.activity.SavedActivity
import com.example.learnmates.ui.activity.SearchActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Reference to the ImageView
        val additionalIcon: ImageView = view.findViewById(R.id.additionalIcon)

        // Set up click listener to show popup menu
        additionalIcon.setOnClickListener {
            showPopupMenu(it)
        }

        return view
    }

    private fun showPopupMenu(view: View) {
        // Create a PopupMenu
        val popupMenu = PopupMenu(requireContext(), view)

        // Inflate the menu resource
        popupMenu.menuInflater.inflate(R.menu.dropdown, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Navigate to HomeActivity
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    Toast.makeText(context, "You are currently in the profile page", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.message -> {
                    // Navigate to MessageActivity
                    val intent = Intent(requireContext(), MessageActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search -> {
                    // Navigate to SearchActivity
                    val intent = Intent(requireContext(), SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.notification -> {
                    // Navigate to NotificationActivity
                    val intent = Intent(requireContext(), NotificationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.feedback -> {
                    // Navigate to FeedbackActivity
                    val intent = Intent(requireContext(), FeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.saved -> {
                    // Navigate to SavedActivity
                    val intent = Intent(requireContext(), SavedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.helpcenter -> {
                    // Navigate to HelpCenterActivity
                    val intent = Intent(requireContext(), HelpCenterActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }
}
