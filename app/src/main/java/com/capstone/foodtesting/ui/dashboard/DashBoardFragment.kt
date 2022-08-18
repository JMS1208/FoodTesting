package com.capstone.foodtesting.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.foodtesting.databinding.BottomSheetListSortingBinding
import com.capstone.foodtesting.databinding.FragmentDashBoardBinding
import com.capstone.foodtesting.ui.bottomsheet.BSRecommendFragment
import com.capstone.foodtesting.ui.bottomsheet.setaddress.BSSetupAddrFragment
import com.capstone.foodtesting.ui.dashboard.category.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashBoardViewModel>()


    private val args by navArgs<DashBoardFragmentArgs>()

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

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestAddressInfo.collectLatest { addressInfo ->

                    binding.tvCurrentAddress.text = addressInfo?.address?.addressFullName ?: "주소 설정하러 가기"

                }
            }
        }



        setupCategory(args.category)
    }

    private fun setupCategory(category: String?){

        if(category == null) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
        } else {
            val index = when(category){
                "한식"-> binding.tabLayout.getTabAt(1)
                "중식"-> binding.tabLayout.getTabAt(2)
                "일식"-> binding.tabLayout.getTabAt(3)
                "양식"-> binding.tabLayout.getTabAt(4)
                "패스트푸드"->binding.tabLayout.getTabAt(5)
                "분식"->binding.tabLayout.getTabAt(6)
                "디저트"->binding.tabLayout.getTabAt(7)
                "기타"-> binding.tabLayout.getTabAt(8)
                else->binding.tabLayout.getTabAt(0)
            }
            binding.tabLayout.selectTab(index)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}