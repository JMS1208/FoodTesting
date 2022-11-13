package com.capstone.foodtesting.ui.common.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ItemBannerAllBinding

class BannerAdapter(val items: List<String>): RecyclerView.Adapter<BannerAdapter.ViewHolder>() {
    inner class ViewHolder(val itemBinding: ItemBannerAllBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(url: String?) {
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.apply {
                strokeWidth = 10f
                centerRadius = 40f
                setTint(itemView.context.resources.getColor(R.color.bright_grey))
                start()
            }

            url?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(circularProgressDrawable)
                    .transform(CenterCrop())
                    .into(itemBinding.ivBanner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemBannerAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size


}