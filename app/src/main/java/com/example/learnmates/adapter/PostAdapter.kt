package com.example.learnmates.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learnmates.R
import com.example.learnmates.model.PostModel
import com.google.firebase.database.FirebaseDatabase

class PostAdapter(private val postList: List<PostModel>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPostMemberName: TextView = itemView.findViewById(R.id.tvPostMemberName)
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvPostText: TextView = itemView.findViewById(R.id.tvPostText)
        val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val ivLike: ImageView = itemView.findViewById(R.id.ivLike)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val ivSavePost: ImageView = itemView.findViewById(R.id.ivSavePost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        val database = FirebaseDatabase.getInstance().getReference("posts").child(post.postId) // Reference to Firebase post

        // Set Member Name and Username
        holder.tvPostMemberName.text = post.postMemberName
        holder.tvUserName.text = "@${post.userName}"
        holder.tvPostText.text = post.postText

        // Load Image from Cloudinary (or Placeholder)
        if (post.postImage.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(post.postImage)
                .placeholder(R.drawable.baseline_fireplace_24) // Show a placeholder before loading
                .into(holder.ivPostImage)
            holder.ivPostImage.visibility = View.VISIBLE
        } else {
            holder.ivPostImage.visibility = View.GONE
        }

        // Set Like Count
        holder.tvLikeCount.text = "${post.likeCount} Likes"

        // Set Initial Save Post Icon
        holder.ivSavePost.setImageResource(
            if (post.savePost) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        )

        // Handle Like Button Click (Update Firebase)
        holder.ivLike.setOnClickListener {
            val newLikeCount = post.likeCount + 1
            database.child("likeCount").setValue(newLikeCount)
            post.likeCount = newLikeCount
            holder.tvLikeCount.text = "$newLikeCount Likes"
        }

        // Handle Save Post Click (Update Firebase)
        holder.ivSavePost.setOnClickListener {
            val newSaveStatus = !post.savePost
            database.child("savePost").setValue(newSaveStatus)
            post.savePost = newSaveStatus
            holder.ivSavePost.setImageResource(
                if (newSaveStatus) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            )
        }
    }
}
