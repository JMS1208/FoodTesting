package com.capstone.foodtesting.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentDashBoardBinding
import com.capstone.foodtesting.ui.dashboard.category.*
import com.capstone.foodtesting.util.hide
import com.capstone.foodtesting.util.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashBoardViewModel>()


    private val categoryFragments: Array<Fragment> = arrayOf(
        TotalFoodFragment(),
        KoreanFoodFragment(),
        ChineseFoodFragment(),
        WesternFoodFragment(),
        FastFoodFragment(),
        FlourFoodFragment(),
        DessertFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BottomSheetBehavior.from(binding.bottomSheetSettingAddress).apply {
            peekHeight = 0
            state = BottomSheetBehavior.STATE_COLLAPSED
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.btnRecommend.show()
                            val bottomNavigationView: BottomNavigationView? =
                                activity?.findViewById(R.id.bottomNavigationView)
                            bottomNavigationView?.apply {
                                show()
                            }

                        }
                        else -> {
                            binding.btnRecommend.hide()
                            val bottomNavigationView: BottomNavigationView? =
                                activity?.findViewById(R.id.bottomNavigationView)
                            bottomNavigationView?.apply {
                                hide()
                            }

                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            })

        }

        binding.tvCurrentAddress.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetSettingAddress).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categoryFragments.size

            override fun createFragment(position: Int): Fragment {
                return categoryFragments[position]
            }

        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when(position) {
                0-> {
                    tab.text = "전체"
                }
                1-> {
                    tab.text = "한식"
                }
                2-> {
                    tab.text = "중식"
                }
                3-> {
                    tab.text = "양식"
                }
                4-> {
                    tab.text = "패스트푸드"
                }
                5-> {
                    tab.text = "분식"
                }
                6-> {
                    tab.text = "디저트"
                }
            }
        }.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}