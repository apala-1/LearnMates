package com.example.learnmates.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learnmates.R
import com.example.learnmates.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PostAdapter(
    private var postList: MutableList<PostModel>,
    private val onSaveClickListener: (PostModel) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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

        Log.d("PostAdapter", "Binding Post: ${post.postText}") // ✅ Debug log
        holder.tvPostMemberName.text = post.postMemberName
        holder.tvUserName.text = "@${post.userName}"
        holder.tvPostText.text = post.postText

        if (post.postImage.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(post.postImage)
                .placeholder(R.drawable.baseline_fireplace_24)
                .into(holder.ivPostImage)
            holder.ivPostImage.visibility = View.VISIBLE
        } else {
            holder.ivPostImage.visibility = View.GONE
        }

        holder.tvLikeCount.text = "${post.likeCount}"

        checkIfLiked(post.postId, holder.ivLike)

        holder.ivLike.setOnClickListener {
            toggleLike(post, holder)
        }

        // ✅ Fetch the saved state when binding
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userSaveRef = FirebaseDatabase.getInstance().getReference("users")
            .child(userId).child("savedPosts").child(post.postId)

        userSaveRef.get().addOnSuccessListener { snapshot ->
            post.savePost = snapshot.exists()  // Set the correct saved state
            holder.ivSavePost.setImageResource(
                if (post.savePost) R.drawable.baseline_bookmark_24
                else R.drawable.baseline_bookmark_border_24
            )
        }

        // ✅ Handle save/unsave click event
        holder.ivSavePost.setOnClickListener {
            val isSaved = post.savePost  // Get the current save state

            if (isSaved) {
                userSaveRef.removeValue()
                holder.ivSavePost.setImageResource(R.drawable.baseline_bookmark_24)
            } else {
                userSaveRef.setValue(post)
                holder.ivSavePost.setImageResource(R.drawable.baseline_bookmark_border_24)
            }

            post.savePost = !isSaved  // Toggle local state
            onSaveClickListener(post) // Notify fragment/activity
        }
    }

    fun updatePosts(newPosts: List<PostModel>) {
        postList.clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()  // 🔥 Force UI update
        Log.d("PostAdapter", "Adapter updated. Total items: ${postList.size}")
    }



    private fun checkIfLiked(postId: String, likeButton: ImageView) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val likesRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes").child(userId)

        likesRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_24)
            } else {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
            }
        }
    }

    private fun toggleLike(post: PostModel, holder: PostViewHolder) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val postRef = FirebaseDatabase.getInstance().getReference("posts").child(post.postId)
        val likesRef = postRef.child("likes").child(userId)

        likesRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                likesRef.removeValue().addOnSuccessListener {
                    postRef.child("likeCount").get().addOnSuccessListener { countSnapshot ->
                        val currentLikes = countSnapshot.getValue(Int::class.java) ?: 0
                        val newLikeCount = if (currentLikes > 0) currentLikes - 1 else 0

                        postRef.child("likeCount").setValue(newLikeCount)
                        holder.tvLikeCount.text = "$newLikeCount"
                        holder.ivLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                    }
                }
            } else {
                likesRef.setValue(true).addOnSuccessListener {
                    postRef.child("likeCount").get().addOnSuccessListener { countSnapshot ->
                        val currentLikes = countSnapshot.getValue(Int::class.java) ?: 0
                        val newLikeCount = currentLikes + 1

                        postRef.child("likeCount").setValue(newLikeCount)
                        holder.tvLikeCount.text = "$newLikeCount"
                        holder.ivLike.setImageResource(R.drawable.baseline_thumb_up_24)
                    }
                }
            }
        }
    }
}