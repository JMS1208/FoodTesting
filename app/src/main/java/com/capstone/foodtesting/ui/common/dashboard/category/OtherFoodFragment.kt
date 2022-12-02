package com.capstone.foodtesting.ui.common.dashboard.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.foodtesting.databinding.FragmentCommonDashBoardCategoryContentsBinding
import com.capstone.foodtesting.ui.common.dashboard.CategoryRestaurantAdapter
import com.capstone.foodtesting.ui.common.dashboard.CommonDashBoardFragment
import com.capstone.foodtesting.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFoodFragment : Fragment() {

    private var _binding: FragmentCommonDashBoardCategoryContentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryRestaurantAdapter

    private val viewModel by lazy {
        (parentFragment as CommonDashBoardFragment).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommonDashBoardCategoryContentsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryRecyclerView()
        setupSwipeRefreshLayoutListener()
    }
    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val latitude = (parentFragment as CommonDashBoardFragment).latitude?.toDoubleOrNull() ?: Constants.InitLatitude
            val longitude = (parentFragment as CommonDashBoardFragment).longitude?.toDoubleOrNull() ?: Constants.InitLongitude

            viewModel.setupCategoryRestaurantListFromChildFragment("el", latitude, longitude)
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    private fun initCategoryRecyclerView() {
        categoryAdapter = CategoryRestaurantAdapter()
        categoryAdapter.setOnItemClickListener {
            (parentFragment as CommonDashBoardFragment).moveToRestaurantRoom(it.reg_num)
        }

        binding.rvCategoryRestaurant.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.otherFoodRestaurantListLiveData.observe(viewLifecycleOwner) { restaurantList->
            restaurantList?.let {
                categoryAdapter.submitList(it)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}