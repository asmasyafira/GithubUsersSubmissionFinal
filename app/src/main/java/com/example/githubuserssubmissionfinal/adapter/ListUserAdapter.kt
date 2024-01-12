package com.example.githubuserssubmissionfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserssubmission.R
import com.example.githubuserssubmissionfinal.response.Users

class ListUserAdapter(private val userList: List<Users>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgUser : ImageView = itemView.findViewById(R.id.img_user_row)
        val nameUser: TextView = itemView.findViewById(R.id.name_user)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = userList[position]
        Glide.with(holder.itemView.context)
            .load(githubUser.avatarUrl)
            .into(holder.imgUser)
        holder.nameUser.text = githubUser.login
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(githubUser)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Users)
    }
    override fun getItemCount(): Int = userList.size
}