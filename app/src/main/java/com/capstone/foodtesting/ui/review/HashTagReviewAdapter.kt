package com.capstone.foodtesting.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.databinding.ItemReviewHashTagBinding

class HashTagReviewAdapter: ListAdapter<String, HashTagReviewAdapter.ViewHolder>(HashTagDiffCallback) {

    inner class ViewHolder(val itemBinding: ItemReviewHashTagBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hashTagText: String) {
            itemBinding.tvHashTagItem.text = hashTagText
        }
    }


    companion object {
        private val HashTagDiffCallback = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemReviewHashTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hashTagText = currentList[position]
        holder.bind(hashTagText)
    }
}