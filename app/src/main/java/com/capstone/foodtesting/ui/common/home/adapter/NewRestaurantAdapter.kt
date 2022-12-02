package com.capstone.foodtesting.ui.common.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.ItemNewRestaurantBinding

class NewRestaurantAdapter(): ListAdapter<Restaurant, NewRestaurantAdapter.ViewHolder>(newRestaurantDiffCallback) {

    private var itemClickListener: ((Restaurant)-> Unit)? = null

    fun setOnItemClickListener(listener: (Restaurant)->Unit) {
        itemClickListener = listener
    }


    inner class ViewHolder(private val itemBinding: ItemNewRestaurantBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(restaurant: Restaurant) {
            itemBinding.apply {
                Glide.with(itemView.context)
                    .load(restaurant.photoUrl)
                    .into(this.ivFood)

                val nameCategory = "${restaurant.name} [${restaurant.category}]"

                tvRestaurantNameCategory.text = nameCategory

            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(restaurant)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemNewRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        private val newRestaurantDiffCallback = object : DiffUtil.ItemCallback<Restaurant?>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem == newItem
            }

        }
    }
}