package com.capstone.foodtesting.ui.restaurant.review

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.databinding.ItemSimpleReviewBinding
import com.capstone.foodtesting.util.sandboxAnimations
import java.text.SimpleDateFormat
import java.util.*

class SimpleReviewAdapter: ListAdapter<Review, SimpleReviewAdapter.ViewHolder>(
    simpleReviewDiffUtilCallback) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E) hh:mm")

    private var itemClickListener: ((Review) -> Unit)? = null

    fun setOnItemClickListener(listener: (Review)->Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemSimpleReviewBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(simpleReview: Review) {

            simpleReview.let {
                itemBinding.tvWriteDate.text = simpleDateFormat.format(Date(it.post_date))
                itemBinding.tvSimpleContents.text = it.contents
            }

            itemBinding.lvProfile.apply {
                sandboxAnimations()
                playAnimation()
            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(simpleReview)
                }
            }
        }

    }

    companion object {

        private val simpleReviewDiffUtilCallback = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemSimpleReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val simpleReview = currentList[position]
        holder.bind(simpleReview)
    }

}