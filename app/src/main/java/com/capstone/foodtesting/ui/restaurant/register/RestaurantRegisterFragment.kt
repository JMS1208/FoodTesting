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

                var leftBtnText = "이전"
                var rightBtnText = "(${currentPage + 1} / ${fragmentList.size}) 다음"

                when (currentPage) {
                    0 -> { //상호명, 사업자등록번호
                        leftBtnText = "취소"


                    }

                    1 -> { //매장 운영시간 휴무일


                    }

                    2 -> { //음식 카테고리 연락처


                    }
                    3 -> { //매장사진등록

                        rightBtnText = "작성 완료"
                    }


                    else -> { //작성 완료


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
                        CommonFunc.showToast(requireContext(), "사업자 등록번호를 확인해주세요")

                        return@setOnClickListener
                    }

                    val childFragment = viewPagerAdapter.createFragment(0) as CorporateFragment

                    val restaurantName = childFragment.getEtRestaurantName()

                    if (restaurantName.isEmpty()) {
                        CommonFunc.showToast(requireContext(), "상호명을 입력해주세요")
                        return@setOnClickListener
                    }

                    restaurant.name = restaurantName
                    memberInfo?.let {
                        restaurant.customer_id = it.uuid
                    } ?: run {
                        CommonFunc.showToast(requireContext(), "사용자 정보를 파악할 수 없습니다\n 다시 로그인해주세요")

                    }

                }
                1 -> {
                    if (restaurant.openTime == null || restaurant.closeTime == null) {
                        CommonFunc.showToast(requireContext(), "매장 운영 시간을 입력해주세요")
                        return@setOnClickListener
                    }
                    val childFragment = viewPagerAdapter.createFragment(1) as OperatingTimeFragment

                    var dayOff = childFragment.getDayOff()

                    if (dayOff.isEmpty()) {
                        dayOff = "연중무휴"
                    }
                    restaurant.holiday = dayOff
                    restaurant.complexity = "보통"

                }
                2 -> {
                    val childFragment = viewPagerAdapter.createFragment(2) as CategoryTelFragment

                    val telephone = childFragment.getEtTelephoneNumber()

                    if (telephone.isEmpty() || telephone.contains("-")) {
                        CommonFunc.showToast(requireContext(), "하이픈 없이 연락처를 입력해주세요")
                        return@setOnClickListener
                    } else {
                        restaurant.telephoneNumber = telephone
                    }

                    if (restaurant.address == null) {
                        CommonFunc.showToast(requireContext(), "매장의 주소를 설정해주세요")
                        return@setOnClickListener
                    }
                    if (restaurant.address!!.isEmpty()) {
                        CommonFunc.showToast(requireContext(), "매장의 주소를 설정해주세요")
                        return@setOnClickListener

                    }
                }

                3 -> { //매장사진 등록화면
                    Log.d("TAG", "입력된 매장 정보: $restaurant")
                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = viewModel.registerRestaurantInfo(restaurant)

                        if (result.isSuccessful) {
                            result.body()?.message?.let { msg ->


//                                CommonFunc.showToast(requireContext(), "메시지 확인: $msg")
                                when (msg) {
                                    "already registered store" -> {
                                        CommonFunc.showToast(requireContext(), "이미 존재하는 매장입니다")

                                    }
                                    "Success to register" -> {
                                        CommonFunc.showToast(requireContext(), "매장을 성공적으로 등록했습니다")
                                        binding.viewPager2.currentItem += 1
                                    }
                                    "Failed to register" -> {
                                        CommonFunc.showToast(requireContext(), "매장 등록에 실패 했습니다")
                                    }
                                    else -> Unit
                                }

                            }


                        } else {
                            withContext(Dispatchers.Main) {
                                CommonFunc.showToast(requireContext(), "매장 등록에 실패했습니다")
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