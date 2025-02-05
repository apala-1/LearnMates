package com.example.learnmates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.model.SearchModel
import com.example.learnmates.model.UserModel

class SearchAdapter(val context: Context, var data: ArrayList<UserModel>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val uName : TextView = itemView.findViewById((R.id.searchedName))
        val addBtn: Button = itemView.findViewById(R.id.addBtn)
        val remBtn: Button = itemView.findViewById(R.id.remBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = data[position]
        holder.uName.text = user.fullname ?: user.username // ✅ Use correct property

        holder.addBtn.setOnClickListener {

        }

        holder.remBtn.setOnClickListener {

        }


    }
}