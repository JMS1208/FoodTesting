package com.capstone.foodtesting.ui.restaurant.review

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.data.model.review.QuesAnswer
import com.capstone.foodtesting.databinding.ItemQuesAndAnswerBinding
import com.capstone.foodtesting.databinding.ItemReviewByCustomerBinding
import com.capstone.foodtesting.util.sandboxAnimations
import java.text.SimpleDateFormat
import java.util.*

class ReviewByCustomerAdapter :
    ListAdapter<QuesAnswer, ReviewByCustomerAdapter.ViewHolder>(diffCallback) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E) hh:mm")
    inner class ViewHolder(private val itemBinding: ItemQuesAndAnswerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(quesAnswer: QuesAnswer) {

            quesAnswer.post_date.let {
                itemBinding.tvWriteDate.text = simpleDateFormat.format(Date(it))
            }
            quesAnswer.answer.let {
                itemBinding.tvAnswer.text = it
            }
            quesAnswer.question.let {
                itemBinding.tvQuestion.text = it
            }



        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<QuesAnswer>() {
            override fun areItemsTheSame(oldItem: QuesAnswer, newItem: QuesAnswer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QuesAnswer, newItem: QuesAnswer): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemQuesAndAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quesAnswer = currentList[position]
        holder.bind(quesAnswer)
    }
}