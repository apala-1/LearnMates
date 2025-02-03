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

        // Set the member name and username
        holder.tvPostMemberName.text = post.postMemberName
        holder.tvUserName.text = "@${post.userName}"
        holder.tvPostText.text = post.postText

        // Display image from Cloudinary if available
        if (post.postImage.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(post.postImage)  // Load image URL from Cloudinary
                .into(holder.ivPostImage)
            holder.ivPostImage.visibility = View.VISIBLE
        } else {
            holder.ivPostImage.visibility = View.GONE
        }

        // Set Like Count
        holder.tvLikeCount.text = "${post.likeCount} Likes"

        // Handle Like Button Click
        holder.ivLike.setOnClickListener {
            post.likeCount += 1
            notifyItemChanged(position)
        }

        // Handle Save Post Click
        holder.ivSavePost.setOnClickListener {
            post.savePost = !post.savePost
            holder.ivSavePost.setImageResource(
                if (post.savePost) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_24
            )
        }
    }
}
