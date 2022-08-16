package com.capstone.foodtesting.ui.bottomsheet.setaddress.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.databinding.ItemAddressInfoBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import it.sephiroth.android.library.xtooltip.Tooltip


class AddressInfoAdapter : ListAdapter<AddressInfo, AddressInfoAdapter.ViewHolder>(
    AddressInfoDiffCallback
) {

    private var onItemRemoveListener: ((AddressInfo) -> Unit)? = null
    private var onItemClickListener: ((AddressInfo) -> Unit)? = null

    fun setOnItemRemoveListener(listener: (AddressInfo) -> Unit) {
        onItemRemoveListener = listener
    }

    fun setOnItemClickListener(listener: (AddressInfo) -> Unit) {
        onItemClickListener = listener
    }



    inner class ViewHolder(val itemBinding: ItemAddressInfoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(addressInfo: AddressInfo) {

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(addressInfo)
                }
            }
            itemBinding.apply {
                tvAddress.text = addressInfo.address?.addressFullName ?: "주소 정보 없음"
                val tmpText = "[도로명: ${addressInfo.roadAddress?.roadAddressFullName ?: "주소 정보 없음"}]"
                tvRoadAddress.text = tmpText
                ivDeleteItem.setOnClickListener {
                    onItemRemoveListener?.let {
                        it(addressInfo)
                    }
                }
                tvCurrentGuide.text = if (addressInfo.isFirstItem) {
                    "(현재 위치)"

                } else {
                    ""
                }

            }

        }
    }

    companion object {
        private val AddressInfoDiffCallback = object : DiffUtil.ItemCallback<AddressInfo>() {
            override fun areContentsTheSame(oldItem: AddressInfo, newItem: AddressInfo): Boolean {
                return oldItem.address == newItem.address &&
                        oldItem.roadAddress == newItem.roadAddress &&
                        oldItem.uuid == newItem.uuid &&
                        oldItem.isFirstItem == newItem.isFirstItem
            }

            override fun areItemsTheSame(oldItem: AddressInfo, newItem: AddressInfo): Boolean {
                return oldItem.uuid == newItem.uuid
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemAddressInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressInfo = currentList[position]
        holder.bind(addressInfo)
    }
}