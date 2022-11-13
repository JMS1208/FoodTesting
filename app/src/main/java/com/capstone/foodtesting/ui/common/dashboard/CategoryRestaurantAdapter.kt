package com.capstone.foodtesting.ui.common.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.ItemNewRestaurantBinding

class CategoryRestaurantAdapter(private val items: List<Restaurant>): RecyclerView.Adapter<CategoryRestaurantAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemNewRestaurantBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Restaurant) {
            itemBinding.tvFood.text = item.name

            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(itemBinding.ivFood)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemNewRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}