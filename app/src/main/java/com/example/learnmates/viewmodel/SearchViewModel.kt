package com.example.learnmates.viewmodel

import androidx.lifecycle.ViewModel
import com.example.learnmates.repository.SearchRepository

class SearchViewModel(val repository: SearchRepository) {
    fun getAllProduct(){
        repository.getAllProduct{
                products, success, message ->
            if(success){
                _allrecords.value = searchedName
            }
        }

}
    }