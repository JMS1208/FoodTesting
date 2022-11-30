package com.capstone.foodtesting.ui.member.review.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.databinding.ItemReviewRecordsBinding

class ReviewRecordsAdapter(): ListAdapter<Review, ReviewRecordsAdapter.ViewHolder>(
    reviewRecordDiffCallback) {


    private var itemClickListener: ((Review)-> Unit)? = null

    fun setOnItemClickListener(listener: (Review)->Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemReviewRecordsBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(review: Review) {


            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(review)
                }
            }
        }
    }

    companion object {
        private val reviewRecordDiffCallback = object: DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemReviewRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = currentList[position]
        holder.bind(review)
    }
}