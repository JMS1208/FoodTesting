package com.capstone.foodtesting.ui.restaurant.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.databinding.ItemMenuBinding
import java.text.DecimalFormat

class ModifyMenuAdapter(): ListAdapter<Menu, ModifyMenuAdapter.ViewHolder>(modifyMenuDiffCallback) {

    private var itemLongClickListener: ((Menu, View)->Unit)? = null

    fun setOnItemLongClickListener(listener: (Menu, View)->Unit ) {
        itemLongClickListener = listener
    }

    private var itemClickListener: ((Menu)->Unit)? = null

    fun setOnItemClickListener(listener: (Menu)->Unit ) {
        itemClickListener = listener
    }

    inner class ViewHolder(val itemBinding: ItemMenuBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(menu: Menu) {
            itemBinding.apply {

                tvMenuName.text = menu.menuName

                val decFormat = DecimalFormat("###,###")

                tvHopePrice.text = decFormat.format(menu.hope_price)
                tvSalePrice.text = decFormat.format(menu.discount_price)

                tvSoldOut.visibility = if(menu.is_break == 1) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }

                Glide.with(itemView.context)
                    .load(menu.photoUrl)
                    .into(ivMenuImage)
            }

            itemView.setOnLongClickListener {
                itemLongClickListener?.let {
                    it(menu, itemView)
                }
                true
            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(menu)
                }
            }

            itemView.setBackgroundResource(R.drawable.ripple_bg_item_click)

        }
    }

    companion object {
        private val modifyMenuDiffCallback = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = currentList[position]
        holder.bind(menu)
    }
}