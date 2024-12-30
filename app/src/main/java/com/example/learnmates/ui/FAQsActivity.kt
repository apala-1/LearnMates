package com.example.learnmates.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.R.id

class FAQsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faqs)

        val questionTextViewOne = findViewById<TextView>(id.tvQuestion1)
        val answerTextViewOne = findViewById<TextView>(id.tvAnswer1)

        val questionTextViewThree = findViewById<TextView>(id.tvQuestion3)
        val answerTextViewThree = findViewById<TextView>(id.tvAnswer3)

        val questionTextViewFour = findViewById<TextView>(id.tvQuestion4)
        val answerTextViewFour = findViewById<TextView>(id.tvAnswer4)

        val questionTextViewFive = findViewById<TextView>(id.tvQuestion5)
        val answerTextViewFive = findViewById<TextView>(id.tvAnswer5)

        val questionTextViewSix = findViewById<TextView>(id.tvQuestion6)
        val answerTextViewSix = findViewById<TextView>(id.tvAnswer6)


        questionTextViewOne.setOnClickListener {

            answerTextViewOne.visibility = if (answerTextViewOne.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        questionTextViewThree.setOnClickListener {

            answerTextViewThree.visibility = if (answerTextViewThree.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        questionTextViewFour.setOnClickListener {

            answerTextViewFour.visibility = if (answerTextViewFour.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        questionTextViewFive.setOnClickListener {

            answerTextViewFive.visibility = if (answerTextViewFive.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        questionTextViewSix.setOnClickListener {

            answerTextViewSix.visibility = if (answerTextViewSix.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}