package com.capstone.foodtesting.ui.home.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.BottomSheetBannerBinding
import com.capstone.foodtesting.databinding.ItemBannerAllBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BSBannerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageList: List<String>

    private lateinit var bannerAdapter: BannerAdapter

    private inner class BannerAdapter(val items: List<String>): RecyclerView.Adapter<BannerAdapter.ViewHolder>() {
        inner class ViewHolder(val itemBinding: ItemBannerAllBinding): RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(url: String?) {
                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.apply {
                    strokeWidth = 10f
                    centerRadius = 40f
                    setTint(resources.getColor(R.color.bright_grey))
                    start()
                }

                url?.let {
                    Glide.with(requireContext())
                        .load(it)
                        .placeholder(circularProgressDrawable)
                        .transform(CenterCrop(), RoundedCorners(20))
                        .into(itemBinding.ivBanner)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding = ItemBannerAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int = items.size


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { dialogInterface ->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                behaviour.isHideable = true
                behaviour.isDraggable = true
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = listOf(
                "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
        "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80",
        "https://images.unsplash.com/photo-1609501677070-800d6d157367?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
        "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
        "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"
        ) // 원래 백에서 이미지 리스트 받아와야함

        bannerAdapter = BannerAdapter(imageList)

        binding.rvBanner.adapter = bannerAdapter
        binding.rvBanner.layoutManager = LinearLayoutManager(requireContext())



        binding.tvClose.setOnClickListener {
            this.dismiss()
        }
    }
}