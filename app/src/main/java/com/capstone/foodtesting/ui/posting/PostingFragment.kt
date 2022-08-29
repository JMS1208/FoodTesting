package com.capstone.foodtesting.ui.posting

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentPostingBinding
import com.capstone.foodtesting.ui.posting.adapter.HashTagAdapter
import com.capstone.foodtesting.ui.posting.adapter.MenuAdapter
import com.capstone.foodtesting.ui.posting.adapter.ReviewAdapter
import com.capstone.foodtesting.util.*
import com.google.android.material.tabs.TabLayout
import com.taufiqrahman.reviewratings.Bar
import com.taufiqrahman.reviewratings.BarLabels
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.abs


@AndroidEntryPoint
class PostingFragment : Fragment() {

    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PostingViewModel>()

    private lateinit var hashTagAdapter: HashTagAdapter

    private lateinit var menuAdapter: MenuAdapter

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupRatingReviews()

        setupReviewRecyclerView()

        setupMenuTextColor()

        setupScrollChangeListener()

        setupHashTagRecyclerView()

        setupMenuRecyclerView()
        binding.tvAddNewMenu.setOnClickListener {
            val action = PostingFragmentDirections.actionPostingFragmentToFragmentWrite()
            findNavController().navigate(action)
        }



    }

    private fun setupReviewRecyclerView() {
        val reviewList = listOf("리뷰1","리뷰2","리뷰3","리뷰4","리뷰5","리뷰6","리뷰7","리뷰8","리뷰9","리뷰10")

        reviewAdapter = ReviewAdapter(reviewList)

        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun setupMenuRecyclerView() {
        val menuList = listOf("메뉴1", "메뉴2", "메뉴3", "메뉴4", "메뉴5", "메뉴6", "메뉴7", "메뉴8", "메뉴9", "메뉴10")
        menuAdapter = MenuAdapter(menuList)

        binding.rvMenu.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupHashTagRecyclerView() {
        val hashList = listOf(
            "#해시태그1",
            "#해시태그2",
            "#해시태그3",
            "#해시태그4",
            "#해시태그5",
            "#해시태그6",
            "#해시태그7",
            "#해시태그8",
            "#해시태그9",
            "#해시태그10"
        )
        hashTagAdapter = HashTagAdapter(hashList)

        binding.rvHashTag.apply {
            adapter = hashTagAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

    }

    private fun setupRatingReviews() {
        binding.ratingReviews.apply {

            val labels = arrayOf<String>(
                "5점  ",
                "4점  ",
                "3점  ",
                "2점  ",
                "1점  "
            )
            val colors = intArrayOf(
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")
            )

            val raters = intArrayOf(
                Random().nextInt(100),
                Random().nextInt(100),
                Random().nextInt(100),
                Random().nextInt(100),
                Random().nextInt(100)
            )

            this.createRatingBars(100, labels, colors, raters)

        }
    }


    private fun setupScrollChangeListener() { //스크롤시 Tab Item 자동 변경

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->


                when (scrollY) {
                    0 -> {
                        binding.tabLayout.setScrollPosition(0, 0f, true)
                    }
                    in 1..binding.nestedScrollView.computeDistanceToView(
                        binding.vMenuDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(1, 0f, true)

                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.vMenuDivider
                    )..binding.nestedScrollView.computeDistanceToView(
                        binding.vHashDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(2, 0f, true)
                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.vHashDivider
                    )..binding.nestedScrollView.computeDistanceToView(
                        binding.vRestaurantInfoDivider
                    ) -> {
                        binding.tabLayout.setScrollPosition(3, 0f, true)
                    }
                    else -> {
                        binding.tabLayout.setScrollPosition(4, 0f, true)
                    }
                }

                if (!binding.nestedScrollView.canScrollVertically(1)) {
                    binding.tabLayout.setScrollPosition(4, 0f, true)
                }


            }
        }

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            binding.appBarLayout.setExpanded(false, true)
                            binding.nestedScrollView.smoothScrollTo(0, 0)
                        }
                        1 -> {
                            binding.appBarLayout.setExpanded(false, true)
                            binding.nestedScrollView.smoothScrollTo(0, 1)
                        }
                        2 -> {
                            binding.appBarLayout.setExpanded(false, true)
                            val tabHeight = binding.tabLayout.height
                            val menuDividerY = binding.vMenuDivider.y.toInt()
                            binding.nestedScrollView.smoothScrollTo(0,menuDividerY-tabHeight)
                        }
                        3 -> {
                            binding.appBarLayout.setExpanded(false, true)
                            val tabHeight = binding.tabLayout.height
                            val hashDividerY = binding.vHashDivider.y.toInt()
                            binding.nestedScrollView.smoothScrollTo(0,hashDividerY-tabHeight)
                        }
                        4 -> {
                            binding.appBarLayout.setExpanded(false, true)
                            val tabHeight = binding.tabLayout.height
                            val restaurantDividerY = binding.vRestaurantInfoDivider.y.toInt()
                            binding.nestedScrollView.smoothScrollTo(0,restaurantDividerY-tabHeight)
                        }
                        else -> return
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit

            }
        )

    }


    private fun setupMenuTextColor() { // 앱바 Collapsed 될때 텍스트 색상 변경
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_white_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                //  Collapsed
                binding.tvAddNewMenu.setTextColor(resources.getColor(R.color.grey))
                binding.toolbar.setNavigationIcon(R.drawable.ic_back_24)
                binding.toolbar.title = "식당명 이곳에"
            } else {
                binding.tvAddNewMenu.setTextColor(resources.getColor(R.color.white))
                binding.toolbar.setNavigationIcon(R.drawable.ic_back_white_24)
                binding.toolbar.title = ""
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}