package com.capstone.foodtesting.ui.common.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.databinding.ItemNewMenuBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class NewMenuAdapter(): ListAdapter<Menu, NewMenuAdapter.ViewHolder>(newMenuDiffCallback) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yy.MM.dd")

    private val decimalFormat = DecimalFormat("###,###")

    private var itemClickListener: ((Menu)-> Unit)? = null

    fun setOnItemClickListener(listener: (Menu)-> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val itemBinding: ItemNewMenuBinding): RecyclerView.ViewHolder(itemBinding.root){

        fun bind(menu: Menu){
            Glide.with(itemView.context)
                .load(menu.photoUrl)
                .into(itemBinding.ivFood)

            menu.menuName?.let {
                itemBinding.tvMenuName.text = it
            }
            menu.discount_price?.let {
                itemBinding.tvMenuDiscountPrice.text = decimalFormat.format(it)
            }
            menu.end_date?.let {
                itemBinding.tvMenuEndDate.text = "~"+simpleDateFormat.format(Date(it))
            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(menu)
                }
            }
        }
    }

    companion object {
        private val newMenuDiffCallback = object: DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemNewMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = currentList[position]
        holder.bind(menu)
    }
}