package com.example.learnmates.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R

class MenuDropdownActivity : AppCompatActivity() {
    private lateinit var  menuIcon: ImageView
    private lateinit var  sidebar: View
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_dropdown)
        sidebar = findViewById(R.id.sidebar)
        menuIcon = findViewById(R.id.menuIcon)
        mainLayout = findViewById(R.id.main)


        menuIcon.setOnClickListener{
            toggleSide()
        }

        mainLayout.setOnClickListener{
            if(sidebar.visibility == View.VISIBLE){
                sidebar.visibility = View.GONE
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun toggleSide() {
      if(sidebar.visibility == View.GONE){
          sidebar.visibility = View.VISIBLE
      }else{
          sidebar.visibility = View.GONE
      }
    }
}