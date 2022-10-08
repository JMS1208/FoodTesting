package com.capstone.foodtesting.ui.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val items: List<String>): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(private val itemBinding: ItemFavoriteBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

            fun bind(url: String) {
                Glide.with(itemView.context)
                    .load(url)
                    .into(itemBinding.kbv)

            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = items[position]
        holder.bind(url)
    }

    override fun getItemCount(): Int = items.size


}