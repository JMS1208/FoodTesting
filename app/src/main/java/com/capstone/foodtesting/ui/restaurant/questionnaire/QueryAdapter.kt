package com.capstone.foodtesting.ui.restaurant.questionnaire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.ItemQueryBinding

class QueryAdapter: ListAdapter<QueryLine, QueryAdapter.ViewHolder>(queryDiffCallback) {

    private var onItemClickListener: ((QueryLine) -> Unit)? = null

    fun setOnItemClickListener(listener: (QueryLine) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(val itemBinding: ItemQueryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(query: QueryLine) {
            itemBinding.tvQuery.text = "\" ${query.query} \""

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(query)
                }
            }
        }
    }


    companion object {
        private val queryDiffCallback = object : DiffUtil.ItemCallback<QueryLine>() {
            override fun areItemsTheSame(oldItem: QueryLine, newItem: QueryLine): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QueryLine, newItem: QueryLine): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryAdapter.ViewHolder {
        val itemBinding = ItemQueryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: QueryAdapter.ViewHolder, position: Int) {
        val query = currentList[position]
        holder.bind(query)
    }


}