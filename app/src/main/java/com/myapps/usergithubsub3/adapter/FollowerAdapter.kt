package com.myapps.usergithubsub3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myapps.usergithubsub3.R
import com.myapps.usergithubsub3.model.User

class FollowerAdapter : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>(){

    private val mFollowerData = ArrayList<User>()

    fun setData(items: ArrayList<User>){
        mFollowerData.clear()
        mFollowerData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following_and_follower, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val data = mFollowerData[position]
        holder.tvUsernName.text = data.userName
        Glide.with(holder.tvUserImage.context).load(data.imageUser).apply(RequestOptions().override(50,50)).into(holder.tvUserImage)
    }

    override fun getItemCount(): Int = mFollowerData.size

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsernName : TextView = itemView.findViewById(R.id.tvUsernameFoll)
        val tvUserImage : ImageView = itemView.findViewById(R.id.tvUserImageFoll)
    }
}