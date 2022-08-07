package com.capstone.foodtesting.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ItemHomeCategoryBinding

//카테고리 목록(이미지포함) 이것도 될 수 있으면 백에서 받아오게 하는게 좋음
//카테고리 클래스 따로 만들어서
//아예 아이콘 이미지까지 받아오는게 맞을듯
class CategoryAdapter(val items: List<String>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(val itemBinding: ItemHomeCategoryBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(category: String) {
            val imageId = when(category) { // 이부분은 나중에 없애야됨
                "한식"-> {
                    R.drawable.ic_korean_food
                }
                "중식"-> {
                    R.drawable.ic_chinese_food
                }
                "일식"-> {
                    R.drawable.ic_japanese_food
                }
                "양식"-> {
                    R.drawable.ic_western_food
                }
                "패스트푸드"->{
                    R.drawable.ic_fast_food
                }
                "분식"->{
                    R.drawable.ic_flour_food
                }
                "디저트"->{
                    R.drawable.ic_dessert
                }
                else-> {
                    R.drawable.ic_other_food
                }
            }
            Glide.with(itemView.context)
                .load(imageId)
                .circleCrop()
                .into(itemBinding.ivCategory)

            itemBinding.tvCategory.text = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}