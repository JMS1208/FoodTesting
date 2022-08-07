package com.capstone.foodtesting.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentHomeBinding
import com.capstone.foodtesting.databinding.ItemBannerBinding
import com.capstone.foodtesting.databinding.ItemHomeCategoryBinding
import com.capstone.foodtesting.ui.home.bottomsheet.BSBannerFragment
import com.capstone.foodtesting.util.Constants.HOME_BANNER_DURATION
import com.capstone.foodtesting.util.Constants.categoryList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import com.capstone.foodtesting.ui.home.adapter.CategoryAdapter
import com.capstone.foodtesting.ui.home.adapter.NewRestaurantPagingAdapter
import com.capstone.foodtesting.ui.home.adapter.ViewPagerAdapter
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var job: Job

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var imageList: MutableList<String>

    private lateinit var newFoodSearchAdapter: NewRestaurantPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        return binding.root

    }

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {

            delay(HOME_BANNER_DURATION)
            val position = binding.viewPager.currentItem
            val newPosition = (position + 1) % imageList.size
            binding.viewPager.setCurrentItem(newPosition, true)

        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageList = mutableListOf(
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80",
            "https://images.unsplash.com/photo-1609501677070-800d6d157367?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1590301157890-4810ed352733?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1336&q=80",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"
        ) // 원래 백에서 이미지 리스트 받아와야함

        val itemSize = imageList.size // 맨 처음 받아왔을때 원래 사이즈


        viewPagerAdapter = ViewPagerAdapter(imageList, itemSize)

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val progressText =
                    "${position % itemSize + 1} / $itemSize 모두보기" // 현재 배너 페이지 표시
                binding.btnBanner.text = progressText

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                when (state) {

                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        job.cancel()
                    }
                    else -> {
                        if (!job.isActive) {
                            scrollJobCreate()
                        }
                    }
                }
            }
        })

        binding.btnBanner.setOnClickListener {

            val bottomSheet = BSBannerFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)


        }



        categoryAdapter = CategoryAdapter(categoryList)

        binding.rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }


        newFoodSearchAdapter = NewRestaurantPagingAdapter()

        binding.rvNewRestaurant.apply {
            setHasFixedSize(true)
            adapter = newFoodSearchAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingResult.collectLatest {
                    newFoodSearchAdapter.submitData(it)
                }
            }
        }


        viewModel.searchFoodsPaging("Korean Food")


    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.root
    }


}