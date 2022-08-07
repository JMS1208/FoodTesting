package com.capstone.foodtesting.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.BottomSheetRecommendBinding
import com.capstone.foodtesting.databinding.BottomSheetSettingAddressBinding
import com.capstone.foodtesting.ui.adapter.CardStackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yuyakaido.android.cardstackview.*


class BSRecommendFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetRecommendBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: CardStackAdapter

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
                behaviour.isDraggable = false
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
        _binding = BottomSheetRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = mutableListOf(
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1609501677070-800d6d157367?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"
        ) // 원래 백에서 이미지 리스트 받아와야함

        cardAdapter = CardStackAdapter(imageList)
        binding.cardStackView.apply {
            layoutManager = CardStackLayoutManager(requireContext(),object: CardStackListener{
                override fun onCardDragging(direction: Direction?, ratio: Float) {

                }

                override fun onCardSwiped(direction: Direction?) {

                }

                override fun onCardRewound() {

                }

                override fun onCardCanceled() {

                }

                override fun onCardAppeared(view: View?, position: Int) {

                }

                override fun onCardDisappeared(view: View?, position: Int) {

                }

            }).apply {
//                setStackFrom(StackFrom.TopAndRight)
//                setVisibleCount(2)
//                setTranslationInterval(12.0F)
//                setScaleInterval(0.9F)
//                setMaxDegree(20.0f)
//                setCanScrollHorizontal(true)
//                setCanScrollVertical(false)
//                setSwipeableMethod(SwipeableMethod.Manual)
                setStackFrom(StackFrom.None)
                setVisibleCount(3)
                setTranslationInterval(8.0f)
                setScaleInterval(0.95f)
                setSwipeThreshold(0.3f)
                setMaxDegree(20.0f)
                setDirections(Direction.HORIZONTAL)
                setCanScrollHorizontal(true)
                setCanScrollVertical(true)
                setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
                setOverlayInterpolator(LinearInterpolator())
            }
           adapter = cardAdapter
            itemAnimator.apply {
                if(this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
        }

        binding.btnClose.setOnClickListener {
            this.dismiss()
        }

        binding.btnVisitRestaurant.setOnClickListener {
            this.dismiss() // 다이얼로그 닫고
            //식당 방문 화면으로 이동
        }
    }



}