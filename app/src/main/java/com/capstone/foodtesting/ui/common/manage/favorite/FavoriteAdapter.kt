package com.capstone.foodtesting.ui.common.manage.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val items: List<Restaurant>): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(private val itemBinding: ItemFavoriteBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

            fun bind(restaurant: Restaurant) {
                Glide.with(itemView.context)
                    .load(restaurant.photoUrl)
                    .into(itemBinding.kbv)

            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = items[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int = items.size


}