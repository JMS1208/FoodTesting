package com.capstone.foodtesting.ui.common.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentCommonHomeBinding
import com.capstone.foodtesting.databinding.ItemBannerBinding
import com.capstone.foodtesting.util.Constants.HOME_BANNER_DURATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import com.capstone.foodtesting.ui.common.home.adapter.CategoryAdapter
import com.capstone.foodtesting.ui.common.home.adapter.NewMenuAdapter
import com.capstone.foodtesting.ui.common.home.adapter.NewRestaurantAdapter
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import com.capstone.foodtesting.util.Constants.CODE_SCAN_BUNDLE_KEY
import com.capstone.foodtesting.util.Constants.CODE_SCAN_REQUEST_KEY
import com.capstone.foodtesting.util.Constants.InitLatitude
import com.capstone.foodtesting.util.Constants.InitLongitude
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CommonHomeFragment : Fragment() {

    private var _binding: FragmentCommonHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CommonHomeViewModel>()

    private lateinit var job: Job

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var categoryAdapter: CategoryAdapter


    private var member: Member? = null

    private lateinit var newRestaurantAdapter: NewRestaurantAdapter

    private lateinit var newMenuAdapter: NewMenuAdapter

    private val colorList = listOf(
        R.drawable.gradient_color_1,
        R.drawable.gradient_color_2,
        R.drawable.gradient_color_1,
        R.drawable.gradient_color_2,
        R.drawable.gradient_color_1,

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
        _binding = FragmentCommonHomeBinding.inflate(inflater, container, false)
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

                binding.ivLogo.apply {
                    if (position % 2 == 0) {
                        setImageResource(R.drawable.ic_logo2)
                    } else {
                        setImageResource(R.drawable.ic_logo)
                    }
                }

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


        //현재 주소를 기준으로 매장 목록들 불러오기
        setupNewRestaurantList()


        binding.btnBanner.setOnClickListener {

//            val bottomSheet = BSBannerFragment()
//            bottomSheet.show(childFragmentManager, bottomSheet.tag)


        }


        newMenuAdapter = NewMenuAdapter()

        newMenuAdapter.setOnItemClickListener { menu->
            menu.regNumber.let {
                val action = CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentRestaurantRoom(it)
                findNavController().navigate(action)
            }
        }

        binding.rvNewMenu.apply {
            setHasFixedSize(true)
            adapter = newMenuAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        newRestaurantAdapter = NewRestaurantAdapter()

        newRestaurantAdapter.setOnItemClickListener { restaurant->
            val action = CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentRestaurantRoom(restaurant.reg_num)
            findNavController().navigate(action)
        }

        binding.rvNewRestaurant.apply {
            setHasFixedSize(true)
            adapter = newRestaurantAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        }


//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.searchPagingResult.collectLatest {
//                    newFoodSearchAdapter.submitData(it)
//                }
//            }
//        }


//        viewModel.searchFoodsPaging("Korean Food")

        binding.tvShowAll.setOnClickListener {
            val action =
                CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentCommonDashBoard()
            findNavController().navigate(action)
        }

        binding.btnQrScanner.setOnClickListener {
            //TODO {아래 지우고 주석 풀어야함}
            val action = CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentCommonCodeScan()
            findNavController().navigate(action)
//            member?.let {
//                val action =
//                    CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentMemberReview(
//                        "111-11-11111",
//                        member!!
//                    )
//                findNavController().navigate(action)
//            }


        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMember().collect {
                member = it
            }

        }

        binding.btnInfo.setOnClickListener {
            val action =
                CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentCommonManage()
            findNavController().navigate(action)
        }

        binding.ivTooltipCategory.setOnClickListener {
            showTooltip(requireContext(), it, "매장들을 카테고리별로 확인해보세요", viewLifecycleOwner)
        }

        binding.ivTooltipExisting.setOnClickListener {
            showTooltip(requireContext(), it, "가장 최근에 등록된 개발 메뉴들입니다", viewLifecycleOwner)
        }



        setupCategoryBtnTouchListener()

        setupFragmentResultListener()


//        binding.ivLogo.setOnClickListener {
//            val dialog = DialogLoading()
//
//            dialog.apply {
//                isCancelable = false
//            }.show(childFragmentManager, dialog.tag)
//        }


        setupNewMenuList()

    }

    private fun setupNewMenuList() {
        viewModel.fetchNewMenuListLiveData()

        viewModel.newMenuListLiveData.observe(viewLifecycleOwner) { newMenuList->
            newMenuList?.let {
                newMenuAdapter.submitList(it)
            }
        }
    }

    private fun setupNewRestaurantList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.latestAddressInfo.collectLatest { addressInfo ->
                val addressX = addressInfo?.x ?: InitLongitude.toString()
                val addressY = addressInfo?.y ?: InitLatitude.toString()
                //x,y 가져와서 요청하고
                viewModel.requestHomeRestaurantList(addressX, addressY)

                val addressName = if(addressX != InitLongitude.toString() && addressY != InitLatitude.toString()) {
                    addressInfo?.address?.addressFullName
                } else{
                    "서울시 동작구 흑석로 84"
                }

                addressName?.let {
                    binding.tvAddressCriteria1.text = "\'$it\' 기준"
                }



            }
        }

        //신규매장목록 들어오는거 관찰
        viewModel.newRestaurantListLiveData.observe(viewLifecycleOwner) { restaurantList->
            restaurantList?.let {
                newRestaurantAdapter.submitList(it)
            }
        }
    }



private fun setupFragmentResultListener() {
    requireActivity().supportFragmentManager.setFragmentResultListener(
        CODE_SCAN_REQUEST_KEY,
        viewLifecycleOwner
    ) { _, bundle ->

        //정보를 꺼내와서
        val reg_num: String? = bundle.getString(CODE_SCAN_BUNDLE_KEY)

        reg_num?.let {
            //TODO 일단은 매장명 전달하는걸로 함 나중에 바꿀것 INFO도 똑같이 해야하는거 잊지말기
            val action =
                CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentMemberReview(
                    it,
                    member!!
                )
            findNavController().navigate(action)

        }
    }
}

private fun setupInitialText() {
    binding.tvBannerItemTitle.text = bannerTitleList[0]
    binding.tvBannerItemContent.text = bannerContentList[0]
}

@SuppressLint("ClickableViewAccessibility")
private fun setupCategoryBtnTouchListener() {
    binding.pvCategoryHansik.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("한식")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryIlsik.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("일식")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryJungsik.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("중식")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryFastfood.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("패스트푸드")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryYangsik.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("양식")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryBunsik.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("분식")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryDessert.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("디저트")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
    binding.pvCategoryOthers.apply {
        setScaleLevels(1f, 1.1f, 1.2f)
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    setScale(1.2f, true)
                }
                MotionEvent.ACTION_UP -> {
                    //여기서 처리
                    navigateFragment("기타")
                }
                MotionEvent.ACTION_MOVE -> Unit
                else -> {
                    setScale(1f, true)
                }
            }
            true
        }
    }
}

private fun navigateFragment(category: String) {
    val action =
        CommonHomeFragmentDirections.actionFragmentCommonHomeToFragmentCommonDashBoard(category)
    findNavController().navigate(action)
}


override fun onDestroyView() {
    super.onDestroyView()
    binding.root
}


}