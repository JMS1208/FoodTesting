package com.capstone.foodtesting.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ItemBannerBinding

class ViewPagerAdapter(
    private val items: MutableList<String>,
    private val itemSize: Int
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    inner class ViewHolder(val itemBinding: ItemBannerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

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
                    .into(itemBinding.ivBannerHome)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val newPosition = position % itemSize
        val item = items[newPosition]
        holder.bind(item)
        // TODO {나중에 시간되면, 요기요처럼 바꾸기}
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}