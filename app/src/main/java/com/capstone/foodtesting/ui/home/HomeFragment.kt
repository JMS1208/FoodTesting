package com.capstone.foodtesting.ui.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentHomeBinding
import com.capstone.foodtesting.databinding.ItemBannerBinding
import com.capstone.foodtesting.databinding.ItemHomeCategoryBinding
import com.capstone.foodtesting.ui.home.bottomsheet.BSBannerFragment
import com.capstone.foodtesting.util.Constants.HOME_BANNER_DURATION
import com.capstone.foodtesting.util.Constants.categoryList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import com.capstone.foodtesting.ui.home.adapter.CategoryAdapter
import com.capstone.foodtesting.ui.home.adapter.NewRestaurantPagingAdapter
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var job: Job

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var categoryAdapter: CategoryAdapter


    private lateinit var newFoodSearchAdapter: NewRestaurantPagingAdapter

    private val colorList = listOf(
        R.drawable.gradient_color_1,
        R.drawable.gradient_color_2,
        R.drawable.gradient_color_3,
        R.drawable.gradient_color_4,
        R.drawable.gradient_color_5
    )
    private val bannerImageList = listOf(
        R.drawable.banner_image1,
        R.drawable.banner_image2,
        R.drawable.banner_image3,
        R.drawable.banner_image4,
        R.drawable.banner_image5
    )

    private val bannerTitleList = listOf(
            "맛있는 가츠동 !",
        "칼칼한 낙지탕",
        "리얼 딸기 케익",
        "수제 돈까스 도시락 !",
        "알싸하게 매운 낚지볶음"

    )

    private val bannerContentList = listOf(
        "따뜻하고 든든하게",
        "바다의 산삼 낙지로\n겨울철 따뜻하게\n보내세요!",
        "상큼한 요거트와 딸기가 가득!",
        "육즙 가득 돈까스 드셔보세요",
        "중독성 있는 매운맛"
    )

    private var scrollState = 0
    private var targetPage = 0
    private var previousPage = 0


    inner class ViewPagerAdapter(private val colorList: List<Int>) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

        inner class ViewHolder(private val itemBinding: ItemBannerBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(colorId: Int) {
                itemBinding.bgColor.background =
                    ResourcesCompat.getDrawable(resources, colorId, null)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding =
                ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val colorId = colorList[position]
            holder.bind(colorId)
        }

        override fun getItemCount(): Int = colorList.size
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        return binding.root

    }

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {

            delay(HOME_BANNER_DURATION)
            val position = binding.viewPager.currentItem
            val newPosition = (position + 1) % bannerImageList.size
            binding.viewPager.setCurrentItem(newPosition, true)

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupInitialText()

        viewPagerAdapter = ViewPagerAdapter(colorList)


        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val itemSize = bannerImageList.size
                val progressText =
                    "${position % itemSize + 1} / $itemSize" // 현재 배너 페이지 표시
                binding.btnBanner.text = progressText

                targetPage = position

                val imageId = bannerImageList[position]
                binding.ivBannerItem.apply {
                    setImageResource(imageId)
                }

                val title = bannerTitleList[position]
                binding.tvBannerItemTitle.text = title

                val content = bannerContentList[position]
                binding.tvBannerItemContent.text = content

                if (previousPage < targetPage) {
                    binding.ivBannerItem.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_right
                        )
                    )
                    binding.tvBannerItemTitle.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_right
                        )
                    )
                    binding.tvBannerItemContent.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_right
                        )
                    )

                } else {
                    binding.ivBannerItem.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_left
                        )
                    )
                    binding.tvBannerItemTitle.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_left
                        )
                    )
                    binding.tvBannerItemContent.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.sticking_on_left
                        )
                    )
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                when (state) {

                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        job.cancel()
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        previousPage = targetPage
                    }
                    else -> {
                        if (!job.isActive) {
                            scrollJobCreate()
                            scrollState = state
                        }
                    }
                }
            }
        })



        binding.btnBanner.setOnClickListener {

            val bottomSheet = BSBannerFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)


        }



//        categoryAdapter = CategoryAdapter(categoryList)

//        binding.rvCategory.apply {
//            adapter = categoryAdapter
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        }

        newFoodSearchAdapter = NewRestaurantPagingAdapter()

        binding.rvNewRestaurant.apply {
            setHasFixedSize(true)
            adapter = newFoodSearchAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingResult.collectLatest {
                    newFoodSearchAdapter.submitData(it)
                }
            }
        }


        viewModel.searchFoodsPaging("Korean Food")

        binding.tvShowAll.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentDashBoard()
            findNavController().navigate(action)
        }

        binding.btnQrScanner.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToCodeScanFragment()
            findNavController().navigate(action)
        }

        binding.btnInfo.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentInfo()
            findNavController().navigate(action)
        }

        binding.ivTooltipCategory.setOnClickListener {
            showTooltip(requireContext(), it, "카테고리 설명 추가")
        }

        binding.ivTooltipExisting.setOnClickListener {
            showTooltip(requireContext(), it,"신메뉴 설명 추가")
        }

        binding.ivTooltipHash1.setOnClickListener {
            showTooltip(requireContext(), it,"해시태그1 설명 추가")
        }

        binding.ivTooltipHash2.setOnClickListener {
            showTooltip(requireContext(), it, "해시태그2 설명 추가")
        }


        setupCategoryBtnTouchListener()



    }

    private fun setupInitialText() {
        binding.tvBannerItemTitle.text = bannerTitleList[0]
        binding.tvBannerItemContent.text = bannerContentList[0]
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCategoryBtnTouchListener() {
        binding.pvCategoryHansik.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("한식")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryIlsik.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("일식")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryJungsik.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("중식")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryFastfood.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("패스트푸드")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryYangsik.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("양식")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryBunsik.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("분식")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryDessert.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("디저트")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
        binding.pvCategoryOthers.apply {
            setScaleLevels(1f,1.1f,1.2f)
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->{
                        setScale(1.2f, true)
                    }
                    MotionEvent.ACTION_UP-> {
                        //여기서 처리
                        navigateFragment("기타")
                    }
                    MotionEvent.ACTION_MOVE-> Unit
                    else-> {
                        setScale(1f, true)
                    }
                }
                true
            }
        }
    }

    private fun navigateFragment(category: String) {
        val action = HomeFragmentDirections.actionFragmentHomeToFragmentDashBoard(category)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.root
    }


}