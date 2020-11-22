package com.fizarum.get10kusd.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fizarum.get10kusd.databinding.ItemUserBinding
import com.fizarum.get10kusd.domain.entities.User

class UsersAdapter(
    private val layoutInflater: LayoutInflater,
    private val listener: EditUserClickListener
) :
    RecyclerView.Adapter<UserItemVH>() {

    private var users: List<Pair<User, Int>> = emptyList()

    fun setUsersWithDays(users: List<Pair<User, Int>>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemVH {
        val itemBinding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserItemVH(itemBinding)
    }

    override fun onBindViewHolder(holder: UserItemVH, position: Int) {
        users[position].let { (user, days) ->
            holder.itemBinding.user = user
            holder.itemBinding.daysToReachTheGoal = days

            holder.itemBinding.ivEdit.setOnClickListener {
                listener.onUserEditInitiated(user)
            }
        }
    }

    override fun getItemCount() = users.size
}