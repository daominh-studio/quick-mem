package com.daominh.quickmem.adapter.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.data.dao.UserDAO
import com.daominh.quickmem.data.model.User
import com.daominh.quickmem.databinding.ItemUserBinding
import com.squareup.picasso.Picasso

class UserClassAdapter(
    private val userList: ArrayList<User>,
    private val isAddMember: Boolean = false,
    private val classId: String = "",
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
        val userDAO = UserDAO(holder.itemView.context)
        holder.binding.userNameTv.text = user.username
        if (user.avatar != null && user.avatar.isNotEmpty()) {
            Picasso.get().load(user.avatar).into(holder.binding.userIv)
        } else {
            Picasso.get().load("https://i.imgur.com/2xW3YHZ.png").into(holder.binding.userIv)
        }
        val groupDAO = GroupDAO(holder.itemView.context)
        val group = groupDAO.getGroupById(classId)
        if (group.user_id == user.id) {
            holder.binding.isAdminTv.visibility = ViewGroup.VISIBLE
        }
        if (isAddMember) {
            updateBackground(holder, user, UserDAO(holder.itemView.context))
            holder.binding.constraintLayout.setOnClickListener {
                if (userDAO.checkUserInClass(user.id, classId)) {
                    holder.binding.constraintLayout.background =
                        AppCompatResources.getDrawable(
                            holder.itemView.context,
                            com.daominh.quickmem.R.drawable.background_unselect
                        )
                    userDAO.removeUserFromClass(user.id, classId)
                } else {
                    holder.binding.constraintLayout.background =
                        AppCompatResources.getDrawable(
                            holder.itemView.context,
                            com.daominh.quickmem.R.drawable.background_select
                        )
                    userDAO.addUserToClass(user.id, classId)
                }
            }

        }

    }

    private fun updateBackground(holder: UserClassViewHolder, user: User, userDAO: UserDAO) {

        if (userDAO.checkUserInClass(user.id, classId)) {
            holder.binding.constraintLayout.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_select
                )
        } else {
            holder.binding.constraintLayout.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_unselect
                )
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}