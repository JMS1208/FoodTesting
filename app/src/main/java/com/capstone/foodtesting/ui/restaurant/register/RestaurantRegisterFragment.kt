package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterBinding
import com.capstone.foodtesting.util.CommonFunc
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class RestaurantRegisterFragment : Fragment() {

    private var _binding: FragmentRestaurantRegisterBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModels<RestaurantRegisterViewModel>()

    private var currentPage = 0
    private var previousPage = 0

    private var regNumIsOk = false

    private val fragmentList = arrayOf(
        CorporateFragment(),
        OperatingTimeFragment(),
        CategoryTelFragment(),
        RestaurantPhotoFragment(),
        RestaurantCompletedFragment()
    )

    fun setRegNumIsOk(value: Boolean) {
        regNumIsOk = value
    }

    private val restaurant: Restaurant = Restaurant(reg_num = UUID.randomUUID().toString())

    private var memberInfo: Member? = null


    private val viewPagerAdapter by lazy {
        object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

        }
    }

    fun getCurrentRestaurant(): Restaurant {
        return restaurant
    }

    fun restaurantInfoUpdate(restaurant: Restaurant) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantRegisterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMemberInfo.collectLatest { member ->
                member?.let {
                    memberInfo = it
                }
            }
        }

        binding.viewPager2.adapter = viewPagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {

                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        previousPage = currentPage
                    }
                    else -> Unit
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position

                var leftBtnText = "??????"
                var rightBtnText = "(${currentPage + 1} / ${fragmentList.size}) ??????"

                when (currentPage) {
                    0 -> { //?????????, ?????????????????????
                        leftBtnText = "??????"


                    }

                    1 -> { //?????? ???????????? ?????????


                    }

                    2 -> { //?????? ???????????? ?????????


                    }
                    3 -> { //??????????????????

                        rightBtnText = "?????? ??????"
                    }


                    else -> { //?????? ??????


                    }
                }


                binding.llBottomBtns.visibility = if (currentPage == fragmentList.lastIndex) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                binding.tvLeft.text = leftBtnText
                binding.tvRight.text = rightBtnText
            }
        })

        binding.tvRight.setOnClickListener {
            when (binding.viewPager2.currentItem) {
                0 -> {
                    if (!regNumIsOk) {
                        CommonFunc.showToast(requireContext(), "????????? ??????????????? ??????????????????")

                        return@setOnClickListener
                    }

                    val childFragment = viewPagerAdapter.createFragment(0) as CorporateFragment

                    val restaurantName = childFragment.getEtRestaurantName()

                    if (restaurantName.isEmpty()) {
                        CommonFunc.showToast(requireContext(), "???????????? ??????????????????")
                        return@setOnClickListener
                    }

                    restaurant.name = restaurantName
                    memberInfo?.let {
                        restaurant.customer_id = it.uuid
                    } ?: run {
                        CommonFunc.showToast(requireContext(), "????????? ????????? ????????? ??? ????????????\n ?????? ?????????????????????")

                    }

                }
                1 -> {
                    if (restaurant.openTime == null || restaurant.closeTime == null) {
                        CommonFunc.showToast(requireContext(), "?????? ?????? ????????? ??????????????????")
                        return@setOnClickListener
                    }
                    val childFragment = viewPagerAdapter.createFragment(1) as OperatingTimeFragment

                    var dayOff = childFragment.getDayOff()

                    if (dayOff.isEmpty()) {
                        dayOff = "????????????"
                    }
                    restaurant.holiday = dayOff
                    restaurant.complexity = "??????"

                }
                2 -> {
                    val childFragment = viewPagerAdapter.createFragment(2) as CategoryTelFragment

                    val telephone = childFragment.getEtTelephoneNumber()

                    if (telephone.isEmpty() || telephone.contains("-")) {
                        CommonFunc.showToast(requireContext(), "????????? ?????? ???????????? ??????????????????")
                        return@setOnClickListener
                    } else {
                        restaurant.telephoneNumber = telephone
                    }

                    if (restaurant.address == null) {
                        CommonFunc.showToast(requireContext(), "????????? ????????? ??????????????????")
                        return@setOnClickListener
                    }
                    if (restaurant.address!!.isEmpty()) {
                        CommonFunc.showToast(requireContext(), "????????? ????????? ??????????????????")
                        return@setOnClickListener

                    }
                }

                3 -> { //???????????? ????????????
                    Log.d("TAG", "????????? ?????? ??????: $restaurant")
                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = viewModel.registerRestaurantInfo(restaurant)

                        if (result.isSuccessful) {
                            result.body()?.message?.let { msg ->


//                                CommonFunc.showToast(requireContext(), "????????? ??????: $msg")
                                when (msg) {
                                    "already registered store" -> {
                                        CommonFunc.showToast(requireContext(), "?????? ???????????? ???????????????")

                                    }
                                    "Success to register" -> {
                                        CommonFunc.showToast(requireContext(), "????????? ??????????????? ??????????????????")
                                        binding.viewPager2.currentItem += 1
                                    }
                                    "Failed to register" -> {
                                        CommonFunc.showToast(requireContext(), "?????? ????????? ?????? ????????????")
                                    }
                                    else -> Unit
                                }

                            }


                        } else {
                            withContext(Dispatchers.Main) {
                                CommonFunc.showToast(requireContext(), "?????? ????????? ??????????????????")
                            }


                        }
                    }


                }


            }

            if (binding.viewPager2.currentItem != 3) {
                binding.viewPager2.currentItem += 1
            }

        }
        binding.tvLeft.setOnClickListener {
            if (binding.viewPager2.currentItem > 0) {
                binding.viewPager2.currentItem -= 1
            } else {
                findNavController().popBackStack()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun makeQuestionnaire() {

    }


}