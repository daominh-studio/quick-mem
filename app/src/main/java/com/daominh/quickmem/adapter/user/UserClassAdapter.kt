package com.daominh.quickmem.adapter.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.User
import com.daominh.quickmem.databinding.ItemUserBinding
import com.squareup.picasso.Picasso

class UserClassAdapter(
    private val userList: ArrayList<User>,
) : RecyclerView.Adapter<UserClassAdapter.UserClassViewHolder>() {
    class UserClassViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserClassViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserClassViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.userNameTv.text = user.username
        Log.d("UserClassAdapter", "onBindViewHolder: ${user.avatar}")
        if (user.avatar != null && user.avatar.isNotEmpty()) {
            Picasso.get().load(user.avatar).into(holder.binding.userIv)
        }  else {
            Picasso.get().load("https://i.imgur.com/2xW3YHZ.png").into(holder.binding.userIv)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}