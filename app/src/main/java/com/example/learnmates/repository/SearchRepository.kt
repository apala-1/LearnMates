package com.example.learnmates.repository

import com.example.learnmates.model.SearchModel

interface SearchRepository {

    fun getAllProduct(callback:
                          (List<SearchModel>?,Boolean,
                           String) -> Unit)

}