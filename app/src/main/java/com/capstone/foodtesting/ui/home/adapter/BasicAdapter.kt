package com.capstone.foodtesting.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BasicAdapter<T>(private val items: List<T>, private val itemBinding: ViewBinding) :
    RecyclerView.Adapter<BasicAdapter<T>.ViewHolder>() {

    private var listener: (T)-> Unit = {
        throw ExceptionInInitializerError("listener 초기화 먼저하고 써야함")
    }

    fun setupItemProcessing(listener: (T) -> Unit) {
        this.listener = listener
    }

    inner class ViewHolder() : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: T) {
            listener(item)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

}