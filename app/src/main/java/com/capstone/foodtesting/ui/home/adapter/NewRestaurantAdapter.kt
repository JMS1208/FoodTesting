package com.capstone.foodtesting.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.databinding.ItemNewRestaurantBinding

class NewRestaurantAdapter(private val items: List<String>): RecyclerView.Adapter<NewRestaurantAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemNewRestaurantBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: String) {
            itemBinding.tvFood.text = item
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