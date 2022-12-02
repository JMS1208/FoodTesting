package com.capstone.foodtesting.ui.member.review.records

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.data.model.review.myreview.Answer
import com.capstone.foodtesting.data.model.review.myreview.MyReviews
import com.capstone.foodtesting.databinding.ItemQuesAndAnswerBinding
import com.capstone.foodtesting.databinding.ItemReviewRecordsForRestaurantBinding
import com.capstone.foodtesting.util.sandboxAnimations
import java.text.SimpleDateFormat
import java.util.*

class ReviewRecordsAdapter(): ListAdapter<MyReviews, ReviewRecordsAdapter.ViewHolder>(
    reviewRecordDiffCallback) {


    private var itemClickListener: ((MyReviews)-> Unit)? = null

    fun setOnItemClickListener(listener: (MyReviews)->Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemReviewRecordsForRestaurantBinding): RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var answerAdapter: AnswerAdapter

        fun bind(myReviews: MyReviews) {

            answerAdapter = AnswerAdapter()

            itemBinding.rvReviewsForRestaurant.apply {
                adapter = answerAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }

            itemBinding.tvToggle.setOnClickListener {
                itemBinding.rvReviewsForRestaurant.visibility = if(itemBinding.rvReviewsForRestaurant.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            myReviews.answerList?.let {
                answerAdapter.submitList(it)
            }

            myReviews.restaurant?.apply {
                itemBinding.tvRestaurantNameCategory.text = "$name [$category]"

                Glide.with(itemView.context)
                    .load(photoUrl)
                    .into(itemBinding.ivRestaurantImage)

            }
            itemBinding.lvRate.apply {
                sandboxAnimations()
                playAnimation()
            }


            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(myReviews)
                }
            }
        }
    }

    companion object {
        private val reviewRecordDiffCallback = object: DiffUtil.ItemCallback<MyReviews>() {
            override fun areItemsTheSame(oldItem: MyReviews, newItem: MyReviews): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MyReviews, newItem: MyReviews): Boolean {
                return oldItem == newItem
            }

        }

        private val answerQuesDiffCallback = object: DiffUtil.ItemCallback<Answer>() {
            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemReviewRecordsForRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = currentList[position]
        holder.bind(review)
    }

    inner class AnswerAdapter: ListAdapter<Answer, AnswerViewHolder>(answerQuesDiffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
            val itemBinding = ItemQuesAndAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AnswerViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
            val answer = currentList[position]
            holder.bind(answer)
        }


    }

    inner class AnswerViewHolder(private val itemBinding: ItemQuesAndAnswerBinding): RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SimpleDateFormat")
        private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E) hh:mm:ss")

        fun bind(answer: Answer) {
            answer.reviewDate?.let {
                itemBinding.tvWriteDate.text = simpleDateFormat.format(Date(it))
            }
            answer.answer?.let {
                itemBinding.tvAnswer.text = it
            }
            answer.question?.let {
                itemBinding.tvQuestion.text = it
            }

        }
    }


}