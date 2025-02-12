package com.example.learnmates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnmates.databinding.ItemUserBinding
import com.example.learnmates.model.UserModel

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = listOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = userList.size

    fun submitList(users: List<UserModel>) {
        userList = users
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserModel) {
            binding.tvUserName.text = user.username
            binding.tvFullName.text = user.fullname  // Use 'fullname' instead of 'fullName'
        }
    }
}