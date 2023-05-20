package com.capstone.foodtesting.ui.common.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.ItemNewRestaurantBinding
import com.capstone.foodtesting.databinding.ItemRestaurantBinding

class CategoryRestaurantAdapter() : ListAdapter<Restaurant, CategoryRestaurantAdapter.ViewHolder>(
    categoryRestaurantDiffCallback
) {

    private var itemClickListener: ((Restaurant) -> Unit)? = null

    fun setOnItemClickListener(listener: (Restaurant) -> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(restaurant: Restaurant) {
            itemBinding.apply {
                Glide.with(itemView.context)
                    .load(restaurant.photoUrl)
                    .into(this.ivFood)
                restaurant.name?.let {
                    tvRestaurantNameCategory.text = it
                }

                restaurant.complexity?.let {
                    tvComplexity.text = it

                    when(it) {
                        "혼잡" -> {
                            tvComplexity.setBackgroundResource(R.drawable.bg_btn_gradient_5)
                        }
                        "보통" -> {
                            tvComplexity.setBackgroundResource(R.drawable.bg_btn_gradient_green_5)
                        }
                        "여유" -> {
                            tvComplexity.setBackgroundResource(R.drawable.bg_btn_gradient_blue_5)
                        }
                    }
                }



            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(restaurant)
                }
            }

        }
    }

    companion object {
        private val categoryRestaurantDiffCallback = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.name == newItem.name && oldItem.complexity == newItem.complexity
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = currentList[position]
        holder.bind(restaurant)
    }
    override fun submitList(list: List<Restaurant>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}