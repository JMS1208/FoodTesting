package com.capstone.foodtesting.ui.posting

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentPostingBinding
import com.capstone.foodtesting.util.computeDistanceToView
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class PostingFragment : Fragment() {

    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PostingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenuTextColor()

        setupScrollChangeListener()

    }


    private fun setupScrollChangeListener() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->


                when (scrollY) {
                    0 -> {
                        binding.tabLayout.setScrollPosition(0, 0f, true)
                    }
                    in 0..binding.nestedScrollView.computeDistanceToView(
                        binding.vMenuDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(1, 0f, true)

                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.vMenuDivider
                    )..binding.nestedScrollView.computeDistanceToView(
                        binding.vHashDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(2, 0f, true)
                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.vHashDivider
                    )..binding.nestedScrollView.computeDistanceToView(
                        binding.vRestaurantInfoDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(3, 0f, true)
                    }
                    else -> {
                        binding.tabLayout.setScrollPosition(4, 0f, true)
                    }
                }

                if (!binding.nestedScrollView.canScrollVertically(1)) {
                    binding.tabLayout.setScrollPosition(4, 0f, true)
                }


            }
        }


    }


    private fun setupMenuTextColor() { // 앱바 Collapsed 될때 텍스트 색상 변경
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_white_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                //  Collapsed
                binding.tvAddNewMenu.setTextColor(resources.getColor(R.color.grey))
                binding.toolbar.setNavigationIcon(R.drawable.ic_back_24)
            } else {
                binding.tvAddNewMenu.setTextColor(resources.getColor(R.color.white))
                binding.toolbar.setNavigationIcon(R.drawable.ic_back_white_24)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}