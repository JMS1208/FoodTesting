package com.capstone.foodtesting.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.BottomSheetListSortingBinding
import com.capstone.foodtesting.databinding.FragmentDashBoardBinding
import com.capstone.foodtesting.ui.bottomsheet.BSRecommendFragment
import com.capstone.foodtesting.ui.bottomsheet.BSSetupAddrFragment
import com.capstone.foodtesting.ui.dashboard.category.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashBoardViewModel>()



    private lateinit var bottomNavigationView: BottomNavigationView

    companion object {

        private val sortingList = listOf(
            "거리순",
            "최신순",
        )

        private val categoryList = arrayOf(
            "전체",
            "한식",
            "중식",
            "일식",
            "양식",
            "패스트푸드",
            "분식",
            "디저트",
            "기타"

        )

        private val categoryFragments = arrayOf(
            TotalFoodFragment(),
            KoreanFoodFragment(),
            ChineseFoodFragment(),
            JapaneseFoodFragment(),
            WesternFoodFragment(),
            FastFoodFragment(),
            FlourFoodFragment(),
            DessertFragment(),
            OtherFoodFragment()
        )
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = activity?.findViewById(R.id.bottomNavigationView)!!


        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categoryFragments.size

            override fun createFragment(position: Int): Fragment {
                return categoryFragments[position]
            }

        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categoryList[position]
        }.attach()



        val bottomSheetSortingBinding = BottomSheetListSortingBinding.inflate(layoutInflater)
        val bottomSheetSortingDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(bottomSheetSortingBinding.root)
        }

        binding.tvSorting.setOnClickListener {
            bottomSheetSortingDialog.apply {
                when(behavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        show()
                    }
                    else -> {
                        hide()
                    }
                }
            }
        }



        binding.tvCurrentAddress.setOnClickListener {
            val bottomSheet = BSSetupAddrFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.btnRecommend.setOnClickListener {
            val bottomSheet = BSRecommendFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}