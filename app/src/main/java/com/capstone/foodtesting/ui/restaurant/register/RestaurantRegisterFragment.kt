package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RestaurantRegisterFragment : Fragment() {

    private var _binding : FragmentRestaurantRegisterBinding? = null
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

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

        }
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
                var rightBtnText = "(${currentPage+1} / ${fragmentList.size}) 다음"

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
            //TODO {아래 주석 풀어야함!}
//            if(binding.viewPager2.currentItem == 0 && !regNumIsOk) {
//                Toast.makeText(requireContext(), "사업자 등록번호를 확인해주세요", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            binding.viewPager2.currentItem += 1
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


}