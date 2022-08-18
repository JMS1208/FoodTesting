package com.capstone.foodtesting.ui.bottomsheet.setaddress.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.databinding.ItemAddressInfoBinding
import com.capstone.foodtesting.databinding.ItemSearchAddressBinding

class AddressSearchPagingAdapter :
    PagingDataAdapter<Document, AddressSearchPagingAdapter.ViewHolder>(AddressSearchDiffCallback) {

    private var itemClickListener: ((Document)-> Unit)? = null

    fun setOnItemClickListener(listener: (Document)-> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(val itemBinding: ItemSearchAddressBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(document: Document?) {


            document?.let {

                itemView.setOnClickListener{
                    itemClickListener?.let {
                        it(document)
                    }
                }

                val addressName: String? = document.addressName
                addressName?.let {
                    itemBinding.tvAddressName.text = addressName

                    document.query?.let { query ->
                        val tmpAddressName = addressName.trim()
                        val tmpQuery = query.trimEnd()
                        if (tmpAddressName.contains(tmpQuery)) {
                            try {
                                val startIndex = addressName.indexOf(tmpQuery)
                                val endIndex = startIndex + tmpQuery.length
                                val span: Spannable = itemBinding.tvAddressName.text as Spannable
                                span.setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    startIndex,
                                    endIndex,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )


                            } catch (E: Exception) {
                                Log.d("TAG", "예외: ${E.message}")
                            }
                        }

                    }


                }
            }


        }
    }

    companion object {
        private val AddressSearchDiffCallback = object : DiffUtil.ItemCallback<Document>() {
            override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
                return oldItem.x == newItem.x &&
                        oldItem.y == newItem.y
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document: Document? = getItem(position)

        holder.bind(document)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSearchAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }
}