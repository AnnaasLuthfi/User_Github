package com.myapps.userfavorite.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myapps.userfavorite.OnItemClick
import com.myapps.userfavorite.R
import com.myapps.userfavorite.activity.DetailActivity
import com.myapps.userfavorite.databinding.ItemListMyFollowingBinding
import com.myapps.userfavorite.model.User

class ListAdapter(private val activity: Activity) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

    var mData = ArrayList<User>()
        set(mData){
            if (mData.size > 0) {
                this.mData.clear()
            }
            this.mData.addAll(mData)
            notifyDataSetChanged()
        }

    fun removeItem(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mData.size)
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_my_following, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListMyFollowingBinding.bind(itemView)
        fun bind(user: User) {
            binding.tvUsername.text = user.userName
            binding.tvidUser.text = user.idUser.toString()
            Glide.with(binding.tvuserImage.context).load(user.imageUser).apply(RequestOptions().override(60, 60)).into(binding.tvuserImage)
            binding.itemListFollow.setOnClickListener(OnItemClick(adapterPosition, object : OnItemClick.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.GETUSERNAME, user.userName)
                    intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                    intent.putExtra(DetailActivity.EXTRA_DATA, user)
                    activity.startActivity(intent)
                }
            }))
        }
    }
}