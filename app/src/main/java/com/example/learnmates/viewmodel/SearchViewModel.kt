package com.example.learnmates.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.learnmates.model.SearchModel
import com.example.learnmates.repository.SearchRepository

class SearchViewModel(val repository: SearchRepository) {
    var _allusers = MutableLiveData<List<SearchModel>?>()

        get() = _allusers


    fun getAllUsers(){
        repository.getAllUsers(){
                users, success, message ->
            if(success){
                _allusers.value = users
            }
        }

    }

}