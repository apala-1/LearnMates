package com.example.learnmates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.R
import com.example.learnmates.model.SearchModel

class SearchAdapter(val context: Context, var data: ArrayList<SearchModel>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val uName : TextView = itemView.findViewById((R.id.searchedName))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.search_user_activity,parent,false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.uName.text = data[position].userNameSearch


    }
}