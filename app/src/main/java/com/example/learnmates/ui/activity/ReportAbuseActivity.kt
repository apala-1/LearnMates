package com.example.learnmates.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnmates.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReportAbuseActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_abuse)

        database = FirebaseDatabase.getInstance().reference.child("abuse_reports")

        val abuseTypeGroup = findViewById<RadioGroup>(R.id.abuseTypeGroup)
        val abuseDescription = findViewById<EditText>(R.id.abuseDescription)
        val contactInfo = findViewById<EditText>(R.id.contactInfo)
        val submitButton = findViewById<Button>(R.id.submitReportButton)

        submitButton.setOnClickListener {
            val selectedId = abuseTypeGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an abuse type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            val abuseType = selectedRadioButton.text.toString()
            val description = abuseDescription.text.toString().trim()
            val contact = contactInfo.text.toString().trim()

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reportId = database.push().key ?: ""
            val reportData = mapOf(
                "id" to reportId,
                "abuseType" to abuseType,
                "description" to description,
                "contactInfo" to contact
            )

            database.child(reportId).setValue(reportData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show()
                    abuseTypeGroup.clearCheck()
                    abuseDescription.text.clear()
                    contactInfo.text.clear()
                } else {
                    Toast.makeText(this, "Failed to submit report", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
