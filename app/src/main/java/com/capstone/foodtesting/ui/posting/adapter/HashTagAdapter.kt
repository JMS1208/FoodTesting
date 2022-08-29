package com.capstone.foodtesting.ui.posting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.databinding.ItemHashTagBinding

class HashTagAdapter(private val hashList: List<String>): RecyclerView.Adapter<HashTagAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ItemHashTagBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hashTag: String) {
            itemBinding.tvHashTagItem.text = hashTag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemHashTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hashTag = hashList[position]
        holder.bind(hashTag)
    }

    override fun getItemCount(): Int = hashList.size
}