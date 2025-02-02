package com.example.learnmates.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.example.learnmates.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpCenterBinding
    private lateinit var resultsLayout: LinearLayout

    private lateinit var gettingStartedTextView: TextView
    private lateinit var faqsTextView: TextView
    private lateinit var contactSupportTextView: TextView
    private lateinit var privacyTextView: TextView
    private lateinit var communityGuidelinesTextView: TextView
    private lateinit var reportAbuseTextView: TextView

    private val dataMap = mapOf(
        "Getting Started" to listOf(),
        "FAQs" to listOf("Update Data", "Delete Data", "Reset Data", "Recover Data", "Contact Support"),
        "Contact Support" to listOf(),
        "Privacy and Security" to listOf(),
        "Community Guidelines" to listOf(),
        "Report Abuse or Misconduct" to listOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the TextViews for the default sections
        gettingStartedTextView = findViewById(R.id.gettingStarted)
        faqsTextView = findViewById(R.id.faqs)
        contactSupportTextView = findViewById(R.id.contactSupport)
        privacyTextView = findViewById(R.id.privacyandsecurity)
        communityGuidelinesTextView = findViewById(R.id.communityguidelines)
        reportAbuseTextView = findViewById(R.id.reportabuse)

        // Dynamically create a LinearLayout for results
        resultsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Add it to the existing ConstraintLayout (main layout)
        val mainLayout = findViewById<View>(R.id.main) as androidx.constraintlayout.widget.ConstraintLayout
        mainLayout.addView(resultsLayout)

        // Set up SearchView listener
        binding.searchText.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterAndDisplayResults(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterAndDisplayResults(it)
                }
                return true
            }
        })
    }

    private fun filterAndDisplayResults(query: String) {
        // Hide the default sections when searching
        gettingStartedTextView.visibility = View.GONE
        faqsTextView.visibility = View.GONE
        contactSupportTextView.visibility = View.GONE
        privacyTextView.visibility = View.GONE
        communityGuidelinesTextView.visibility = View.GONE
        reportAbuseTextView.visibility = View.GONE

        // Clear the dynamically created results layout
        resultsLayout.removeAllViews()

        // Filter the categories and their FAQs
        val filteredResults = mutableListOf<Pair<String, List<String>>>()
        for ((category, faqs) in dataMap) {
            if (category.contains(query, ignoreCase = true)) {
                filteredResults.add(category to faqs)
            } else {
                val filteredFaqs = faqs.filter { it.contains(query, ignoreCase = true) }
                if (filteredFaqs.isNotEmpty()) {
                    filteredResults.add(category to filteredFaqs)
                }
            }
        }

        // Display the results dynamically
        if (filteredResults.isEmpty()) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
            return
        }

        // Show the filtered results below the search bar
        for ((category, faqs) in filteredResults) {
            val categoryView = TextView(this).apply {
                text = category
                textSize = 20f
                setTextColor(resources.getColor(R.color.black, theme))
                setPadding(16, 16, 16, 8)
                setTypeface(null, android.graphics.Typeface.BOLD)
            }
            resultsLayout.addView(categoryView)

            for (faq in faqs) {
                val faqView = TextView(this).apply {
                    text = "• $faq"
                    textSize = 16f
                    setTextColor(resources.getColor(R.color.blue, theme))
                    setPadding(32, 8, 16, 8)

                    // Set click listener to navigate to respective page
                    setOnClickListener {
                        // For now, just simulate navigation by showing a Toast (can replace with actual navigation logic)
                        val intent = Intent(this@HelpCenterActivity, FAQsActivity::class.java)
                        intent.putExtra("FAQ_Title", faq)
                        startActivity(intent)
                    }
                }
                resultsLayout.addView(faqView)
            }
        }
    }
}
