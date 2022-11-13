package com.capstone.foodtesting.ui.common.dashboard.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentCommonDashBoardCategoryContentsBinding
import com.capstone.foodtesting.ui.common.dashboard.CategoryRestaurantAdapter
import com.capstone.foodtesting.ui.common.dashboard.CommonDashBoardFragment
import com.capstone.foodtesting.ui.common.home.adapter.NewRestaurantAdapter
import com.capstone.foodtesting.util.Constants.InitLatitude
import com.capstone.foodtesting.util.Constants.InitLongitude

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KoreanFoodFragment : Fragment() {

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

        viewLifecycleOwner.lifecycleScope.launch {
            val result = viewModel.getRestaruantByCategory("한식", InitLatitude, InitLongitude)

            if(result.isSuccessful) {
                val restaurantList: List<Restaurant>? = result.body()

                restaurantList?.let {
                    categoryAdapter = CategoryRestaurantAdapter(it)

                    binding.rvSorted.adapter = categoryAdapter
                    binding.rvSorted.layoutManager = GridLayoutManager(requireContext(), 3)

                }

            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "한식삭제")
        _binding = null
    }

}