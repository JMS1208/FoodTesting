package com.capstone.foodtesting.ui.member.review

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ItemKeywordBinding

class KeywordAdapter : ListAdapter<String, KeywordAdapter.ViewHolder>(KeywordDiffCallback) {

    private var onItemRemoveListener: ((String) -> Unit)? = null

    private var onItemClickListener: ((String) -> Unit)? = null

    private var chipCloseEnabled = true



    fun setOnItemRemoveListener(listener: (String) -> Unit) {
        onItemRemoveListener = listener
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setChipCloseBtnEnabled(boolean: Boolean) {
        chipCloseEnabled = boolean
    }






    inner class ViewHolder(val itemBinding: ItemKeywordBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(keyword: String) {
//            val processedKeyword = "#${keyword.removePrefix("#")}"
            itemView.invalidate()

            itemBinding.chipKeyword.apply {
                text = keyword
                isCloseIconVisible = chipCloseEnabled
            }

            itemBinding.chipKeyword.setOnClickListener {
                onItemClickListener?.let {
                    it(keyword)
                }
            }

            itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    itemView.context,
                    R.anim.sticking_on_right
                )
            )

            itemBinding.chipKeyword.setOnCloseIconClickListener {
                itemView.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.disapearing_on_downward))
                onItemRemoveListener?.let {
                    it(keyword)
                }
            }

        }
    }


    companion object {
        private val KeywordDiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyword = currentList[position]
        holder.bind(keyword)

    }
}