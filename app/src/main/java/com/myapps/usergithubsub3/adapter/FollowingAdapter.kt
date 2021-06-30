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

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    val mFollowingData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mFollowingData.clear()
        mFollowingData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following_and_follower, parent, false)
        return FollowingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val data = mFollowingData[position]
        holder.tvUsernName.text = data.userName
        Glide.with(holder.tvUserImage.context).load(data.imageUser).apply(RequestOptions().override(50,50)).into(holder.tvUserImage)
    }

    override fun getItemCount(): Int = mFollowingData.size

    inner class FollowingViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvUsernName = itemView.findViewById<TextView>(R.id.tvUsernameFoll)
        val tvUserImage = itemView.findViewById<ImageView>(R.id.tvUserImageFoll)
    }
}