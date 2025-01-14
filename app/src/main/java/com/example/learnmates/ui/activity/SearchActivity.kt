package com.example.learnmates.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnmates.R
import com.example.learnmates.adapter.SearchAdapter
import com.example.learnmates.databinding.ActivitySearchBinding
import com.example.learnmates.repository.SearchRepositoryImpl
import com.example.learnmates.viewmodel.SearchViewModel
import java.util.ArrayList

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var adapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SearchAdapter(this@SearchActivity, ArrayList())

        var repo = SearchRepositoryImpl()
        searchViewModel= SearchViewModel(repo)

        searchViewModel.getAllUsers()




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}