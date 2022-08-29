package com.capstone.foodtesting.ui.posting.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.databinding.ItemMenuBinding

class MenuAdapter(private val menuList: List<String>): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ItemMenuBinding): RecyclerView.ViewHolder(itemBinding.root) {


        @SuppressLint("ClickableViewAccessibility")
        fun bind(menu: String) {
            itemBinding.tvMenuName.text = menu
            itemBinding.ivMenu.apply {
                setScaleLevels(1f,1.1f,1.2f)
                setOnTouchListener { _, motionEvent ->
                    when(motionEvent.action) {
                        MotionEvent.ACTION_DOWN-> {
                            setScale(1.2f,true)
                        }
                        MotionEvent.ACTION_MOVE-> Unit
                        else -> {
                            setScale(1f, true)
                        }
                    }
                    true
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