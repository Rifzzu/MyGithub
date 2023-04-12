package com.example.mygithub

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithub.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: List<ItemsItem>): RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = listUser[position]
        holder.tvName.text = users.login
        Glide.with(holder.itemView.context)
            .load(users.avatarUrl)
            .circleCrop()
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_USER, users.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
    override fun getItemCount() = listUser.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName : TextView = view.findViewById(R.id.tv_account)
        val imgPhoto : ImageView = view.findViewById(R.id.img_avatar)
    }
}