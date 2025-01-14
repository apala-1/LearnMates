package com.example.learnmates.repository

import com.example.learnmates.model.SearchModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchRepositoryImpl: SearchRepository {
    val database: FirebaseDatabase =
        FirebaseDatabase.getInstance()

    val reference: DatabaseReference = database.reference.child("users")
    override fun getAllUsers(callback: (List<SearchModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var users = mutableListOf<SearchModel>()
                if(snapshot.exists()){
                    for(eachProduct in snapshot.children){
                        var model = eachProduct.getValue(SearchModel::class.java)
                        if(model != null){
                            users.add(model)
                        }
                    }
                    callback(users,true,"fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message.toString())
            }
        })
    }
    }
