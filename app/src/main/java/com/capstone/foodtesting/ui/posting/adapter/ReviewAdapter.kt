package com.capstone.foodtesting.ui.posting.adapter

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.databinding.ItemReviewBinding

class ReviewAdapter(private val reviewList: List<String>): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ItemReviewBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(review: String) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviewList.size

}