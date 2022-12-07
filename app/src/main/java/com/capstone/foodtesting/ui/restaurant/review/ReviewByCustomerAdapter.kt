package com.capstone.foodtesting.ui.restaurant.review

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.data.model.review.QuesAnswer
import com.capstone.foodtesting.data.model.review.reviews.Answer
import com.capstone.foodtesting.data.model.review.reviews.ReviewAll
import com.capstone.foodtesting.databinding.ItemQuesAndAnswerBinding
import com.capstone.foodtesting.databinding.ItemReviewByCustomerBinding
import com.capstone.foodtesting.ui.member.review.records.ReviewRecordsAdapter
import com.capstone.foodtesting.util.emailMasking
import com.capstone.foodtesting.util.sandboxAnimations
import java.text.SimpleDateFormat
import java.util.*

class ReviewByCustomerAdapter :
    ListAdapter<ReviewAll, ReviewByCustomerAdapter.ViewHolder>(diffCallback) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E) hh:mm")

    @SuppressLint("SimpleDateFormat")
    private val yearSimpleDateFormat = SimpleDateFormat("yyyy")

    inner class ViewHolder(private val itemBinding: ItemReviewByCustomerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var answerAdapter: AnswerAdapter

        fun bind(reviewAll: ReviewAll) {

            answerAdapter = AnswerAdapter()

            itemBinding.rvReviewsForRestaurant.apply {
                adapter = answerAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }

            reviewAll.customer?.gender?.let {
                val fileName = when (it) {
                    1 -> { //남자
                        "lottie/boy.json"
                    }
                    2 -> { //여자
                        "lottie/girl.json"
                    }
                    else -> {
                        "lottie/star.json"
                    }
                }
                itemBinding.lvProfile.apply {

                    setAnimation(fileName)
                    sandboxAnimations()
                    playAnimation()
                }

            }

            reviewAll.customer?.nickname?.let {
                itemBinding.tvCustomerName.text = it
            }

            reviewAll.customer?.age?.let {
                try {
                    val today = yearSimpleDateFormat.format(Date(System.currentTimeMillis())).toInt()
                    val year = yearSimpleDateFormat.format(Date(it.toLong())).toInt()
                    itemBinding.tvCustomerName.append(" [${(today-year)/10*10}대]")
                } catch (E: Exception) {
                    Log.e("TAG", E.message.toString())
                }

            }

            reviewAll.customer?.email?.let {
                itemBinding.tvEmail.text = it.emailMasking()
            }

            itemBinding.tvToggle.setOnClickListener {
                itemBinding.rvReviewsForRestaurant.visibility = if(itemBinding.rvReviewsForRestaurant.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            reviewAll.answer?.let {
                answerAdapter.submitList(it)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ReviewAll>() {
            override fun areItemsTheSame(oldItem: ReviewAll, newItem: ReviewAll): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ReviewAll, newItem: ReviewAll): Boolean {
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
        val itemBinding =
            ItemReviewByCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewAll = currentList[position]
        holder.bind(reviewAll)
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


            answer.contents?.let {
                itemBinding.tvQuestion.text = it
            }
            answer.reviewLine?.let {
                itemBinding.tvAnswer.text = it
            }

        }
    }
}