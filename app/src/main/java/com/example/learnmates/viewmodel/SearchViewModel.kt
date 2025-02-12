package com.example.learnmates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnmates.model.UserModel
import com.example.learnmates.repository.SearchRepositoryImpl
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepositoryImpl) : ViewModel() {

     var _allUsers = MutableLiveData<List<UserModel>>()
     var allUsers = MutableLiveData<List<UserModel>>()
         get() = _allUsers


    fun getUsersByName(name: String) {
        viewModelScope.launch {
            val result = repository.searchUsers(name)
            _allUsers.postValue(result)
        }
    }
}
