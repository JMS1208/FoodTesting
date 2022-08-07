package com.capstone.foodtesting.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ItemRecommendBinding

class CardStackAdapter(
    private var items: List<String> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() { // 얘도 잘하면 페이징 될거같음

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(private val itemBinding: ItemRecommendBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(url: String?) {
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.apply {
                strokeWidth = 10f
                centerRadius = 40f
                setTint(itemView.context.resources.getColor(R.color.bright_grey))
                start()
            }

            Log.d("TAG", "url: $url")
            url?.let {

                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(circularProgressDrawable)
                    .transform(CenterCrop(), RoundedCorners(20) )
                    .into(itemBinding.ivRecommend)
            }
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "터치", Toast.LENGTH_SHORT).show()
            }
        }
    }

}