package com.capstone.foodtesting.ui.restaurant.room.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.databinding.ItemMenuBinding
import java.text.DecimalFormat

class MenuAdapter(private val menuList: List<Menu>): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {


    private var itemClickListener: ((Menu) -> Unit)? = null

    fun setOnItemClickListener(listener: (Menu)-> Unit) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(val itemBinding: ItemMenuBinding): RecyclerView.ViewHolder(itemBinding.root) {


        @SuppressLint("ClickableViewAccessibility")
        fun bind(menu: Menu) {

            itemBinding.apply {
                Glide.with(itemView.context)
                    .load(menu.photoUrl)
                    .into(ivMenuImage)

                tvMenuName.text = menu.menuName

                val decFormat = DecimalFormat("###,###")

                tvHopePrice.text = decFormat.format(menu.hope_price)
                tvSalePrice.text = decFormat.format(menu.discount_price)
                tvSoldOut.visibility = if(menu.is_break == 1) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }

            itemView.setOnClickListener {
                itemClickListener?.let {
                    it(menu)
                }
            }

        }
    }

    override fun getItemCount(): Int = menuList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]
        holder.bind(menu)
    }
}