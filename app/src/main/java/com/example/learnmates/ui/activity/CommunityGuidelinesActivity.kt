package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.google.firebase.database.FirebaseDatabase

class CommunityGuidelinesActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_guidelines)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        // Initialize UI elements
        val guidelinesContainer = findViewById<LinearLayout>(R.id.guidelinesContainer)
        val acceptButton = findViewById<Button>(R.id.acceptButton)

        // List of guidelines (To be stored in Firebase)
        val guidelinesList = listOf(
            "1. Be respectful and considerate to others.",
            "2. Keep the content appropriate and educational.",
            "3. Avoid spamming the chat or sharing irrelevant files.",
            "4. Report any inappropriate behavior to admins.",
            "5. Share files only if they are useful for learning.",
            "6. Keep discussions respectful, even in disagreements.",
            "7. Be patient with new learners, and offer constructive feedback.",
            "8. Do not share personal or sensitive information in public spaces.",
            "9. Use appropriate channels for feedback and suggestions.",
            "10. Be honest, and do not attempt to deceive others in any way.",
            "11. Be proactive in sharing helpful resources and study materials.",
            "12. Avoid using offensive language and ensure a friendly atmosphere.",
            "13. Keep all content focused on learning and academic growth.",
            "14. Encourage diverse perspectives and ensure inclusivity.",
            "15. Avoid excessive self-promotion or advertisements."
        )

        // Save guidelines to Firebase Realtime Database
        saveGuidelinesToFirebase(guidelinesList)

        // Dynamically add guidelines to LinearLayout (for display)
        guidelinesList.forEach { guideline ->
            val textView = TextView(this)
            textView.text = guideline
            textView.textSize = 16f
            textView.setTextColor(resources.getColor(R.color.black, null))
            textView.setPadding(0, 8, 0, 8)
            guidelinesContainer.addView(textView)
        }

        // Accept button click listener
        acceptButton.setOnClickListener {
            Toast.makeText(this, "Guidelines accepted!", Toast.LENGTH_SHORT).show()
            finish() // Close activity after accepting
        }
    }

    private fun saveGuidelinesToFirebase(guidelinesList: List<String>) {
        val guidelinesRef = database.getReference("communityGuidelines")

        // Loop through guidelines and store each one in Firebase
        guidelinesList.forEachIndexed { index, guideline ->
            val guidelineId = "guideline$index"  // Use unique key for each guideline
            guidelinesRef.child(guidelineId).setValue(guideline)
                .addOnSuccessListener {
                    // Success callback
                    Toast.makeText(this, "Guideline $guidelineId saved!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Error callback
                    Toast.makeText(this, "Failed to save guideline", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
