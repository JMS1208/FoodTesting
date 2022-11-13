package com.capstone.foodtesting.ui.common.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.databinding.ItemNewRestaurantBinding

class NewRestaurantPagingAdapter : PagingDataAdapter<Result, NewRestaurantPagingAdapter.ViewHolder>(
    FoodDiffCallback) {

    companion object {
        private val FoodDiffCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(val itemBinding: ItemNewRestaurantBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Result) {
            val url = item.urls?.small
            val userName = item.user?.name
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.apply {
                strokeWidth = 10f
                centerRadius = 40f
                setTint(itemView.context.resources.getColor(R.color.bright_grey))
                start()
            }



            Glide.with(itemView.context)
                .load(url)
                .placeholder(circularProgressDrawable)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(itemBinding.ivFood)


            userName?.let {
                itemBinding.tvFood.text = it
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pagedItem: Result? = getItem(position)

        pagedItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}