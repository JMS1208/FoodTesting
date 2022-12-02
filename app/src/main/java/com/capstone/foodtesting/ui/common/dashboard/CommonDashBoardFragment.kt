package com.capstone.foodtesting.ui.common.dashboard

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.BottomSheetListSortingBinding
import com.capstone.foodtesting.databinding.FragmentCommonDashBoardBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.BSSetupAddrFragment
import com.capstone.foodtesting.ui.common.dashboard.category.*
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.Constants
import com.capstone.foodtesting.util.Constants.InitLatitude
import com.capstone.foodtesting.util.Constants.InitLongitude
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.navercorp.nid.NaverIdLoginSDK.behavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonDashBoardFragment : Fragment() {

    private var _binding: FragmentCommonDashBoardBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModels<CommonDashBoardViewModel>()

    private val args by navArgs<CommonDashBoardFragmentArgs>()

    var latitude: String? = null
    var longitude: String? = null


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
        _binding = FragmentCommonDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPagerView()

        initViewClickListener()

        initObserver()

        setupCategory(args.category)

    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestAddressInfo.collectLatest { addressInfo ->
                    binding.tvCurrentAddress.text = addressInfo?.address?.addressFullName ?: "주소 설정하러 가기"

                    addressInfo?.let {

                        try {
                            if(it.y == null || it.x == null) {
                                viewModel.setupCategoryRestaurantList(InitLatitude, InitLongitude)
                            } else {
                                viewModel.setupCategoryRestaurantList(it.y!!.toDouble(), it.x!!.toDouble())
                            }
                        } catch(E: Exception) {
                            Log.e("TAG", "initObserver: ${E.message}" )
                        }
                    } ?: run {
                        viewModel.setupCategoryRestaurantList(InitLatitude, InitLongitude)
                    }
                }
            }
        }


    }

    private fun initViewClickListener() {
        binding.tvCurrentAddress.setOnClickListener {
            val bottomSheet = BSSetupAddrFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewPagerView() {
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categoryFragments.size

            override fun createFragment(position: Int): Fragment {
                return categoryFragments[position]
            }

        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categoryList[position]
        }.attach()
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


    fun moveToRestaurantRoom(regNum: String) {
        val action = CommonDashBoardFragmentDirections.actionFragmentCommonDashBoardToFragmentRestaurantRoom(regNum)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}