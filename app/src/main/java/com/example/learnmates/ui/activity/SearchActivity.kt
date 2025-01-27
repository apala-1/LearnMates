package com.example.learnmates.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learnmates.R
import com.example.learnmates.adapter.SearchAdapter
import com.example.learnmates.databinding.ActivitySearchBinding
import com.example.learnmates.repository.SearchReposiitoryImpl
import com.example.learnmates.viewmodel.SearchViewModel
import java.util.ArrayList

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchbtn.setOnClickListener{
            val searchtext = binding.SearchBar.text.toString()
            if(searchtext.isEmpty()){
                binding.SearchBar.setError("Invalid Input")
            }
            setupSearchRecyclerView(searchtext);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSearchRecyclerView(searchtext: String) {
        adapter = SearchAdapter(this@SearchActivity, ArrayList())
        var repo = SearchReposiitoryImpl()
        SearchViewModel.getAllProduct()


    }
}