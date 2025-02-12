package com.example.learnmates.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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


    private lateinit var aboutMeButton: Button
    private lateinit var aboutMeSection: LinearLayout
    private lateinit var aboutMeText: TextView
    private lateinit var editButton: Button
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        aboutMeButton = rootView.findViewById(R.id.aboutMeButton)
        aboutMeSection = rootView.findViewById(R.id.aboutMeSection)
        aboutMeText = rootView.findViewById(R.id.aboutMeText)
        editButton = rootView.findViewById(R.id.editButton)
        saveButton = rootView.findViewById(R.id.saveButton)


        aboutMeButton.setOnClickListener {
            aboutMeSection.visibility = View.VISIBLE
        }


        editButton.setOnClickListener {

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


        val additionalIcon: ImageView = rootView.findViewById(R.id.additionalIcon)
        additionalIcon.setOnClickListener {
            showPopupMenu(it)
        }

        return rootView
    }

    private fun showPopupMenu(view: View) {

        val popupMenu = PopupMenu(requireContext(), view)


        popupMenu.menuInflater.inflate(R.menu.dropdown, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {

                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    Toast.makeText(context, "You are currently in the profile page", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.message -> {

                    val intent = Intent(requireContext(), MessageActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search -> {

                    val intent = Intent(requireContext(), SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.notification -> {

                    val intent = Intent(requireContext(), NotificationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.feedback -> {

                    val intent = Intent(requireContext(), FeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.saved -> {

                    val intent = Intent(requireContext(), SavedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.helpcenter -> {

                    val intent = Intent(requireContext(), HelpCenterActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        popupMenu.show()
    }
}
