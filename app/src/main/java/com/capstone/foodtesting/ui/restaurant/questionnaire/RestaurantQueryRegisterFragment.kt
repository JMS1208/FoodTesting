package com.capstone.foodtesting.ui.restaurant.questionnaire

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentRestaurantQueryRegisterBinding
import com.capstone.foodtesting.ui.restaurant.questionnaire.add.RestaurantQueryRegisterAddPersonallyFragment
import com.capstone.foodtesting.ui.restaurant.questionnaire.completed.RestaurantQueryRegisterCompletedFragment
import com.capstone.foodtesting.ui.restaurant.questionnaire.menu.RestaurantQueryRegisterAboutTestingMenuFragment
import com.capstone.foodtesting.ui.restaurant.questionnaire.overview.RestaurantQueryRegisterOverviewFragment
import com.capstone.foodtesting.ui.restaurant.questionnaire.restaurant.RestaurantQueryRegisterAboutRestaurantFragment
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.Constants.IS_ALREADY_EXISTED
import com.capstone.foodtesting.util.Constants.IS_SUCCESS
import com.capstone.foodtesting.util.Constants.IS_TOO_MUCH_COUNT
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.min

@AndroidEntryPoint
class RestaurantQueryRegisterFragment : Fragment() {

    private var _binding: FragmentRestaurantQueryRegisterBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RestaurantQueryRegisterFragmentArgs>()


    private val fragmentList = arrayOf(
        RestaurantQueryRegisterOverviewFragment(),
        RestaurantQueryRegisterAboutRestaurantFragment(),
        RestaurantQueryRegisterAboutTestingMenuFragment(),
        RestaurantQueryRegisterAddPersonallyFragment(),
        RestaurantQueryRegisterCompletedFragment()
    )
    private var currentPage = 0

    val viewModel by viewModels<RestaurantQueryRegisterViewModel>()

    private lateinit var questionnaireAdapter: QuestionnaireAdapter


    private var previousPage = 0


    fun getRegNum(): String {
        return args.regNum
    }

    inner class SwipeHelperCallback(private val adapter: QuestionnaireAdapter) :
        ItemTouchHelper.Callback() {

        // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정하기 위한 변수들
        private var currentPosition: Int? = null    // 현재 선택된 recycler view의 position
        private var previousPosition: Int? = null   // 이전에 선택했던 recycler view의 position
        private var currentDx = 0f                  // 현재 x 값
        private var clamp = 0f                      // 고정시킬 크기
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            Log.d("TAG", "getMovementFlags() ")
            return makeMovementFlags(UP or DOWN, LEFT or RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            Log.d("TAG", "onMove()")
            val fromPosition = viewHolder.absoluteAdapterPosition
            val toPosition = target.absoluteAdapterPosition
            viewModel.swapQuery(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when (direction) { //여기서 삭제 또는 수정보이게
                LEFT -> {

                }
                RIGHT -> {

                }
                else -> return
            }

            Log.d("TAG", "onSwiped()")
        }

        //사용자와의 상호작용과 애니메이션도 끝났을 때 호출
        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            currentDx = 0f
            previousPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().clearView(getView(viewHolder))

            Log.d("TAG", "clearView() ")
        }


        //뷰홀더가 스와이프 되었거나 드래그 되었을때 호출
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            viewHolder?.let {
                currentPosition = viewHolder.absoluteAdapterPosition
                getDefaultUIUtil().onSelected(getView(it))

            }
            Log.d("TAG", "onSelectedChanged() ")
        }

        //아이템을 터치하거나 스와이프하는 등 뷰에 변화가 생길 경우 호출
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (actionState == ACTION_STATE_SWIPE) {
                val view = getView(viewHolder)
                val isClamped = getTag(viewHolder)
                val newX = clampViewPositionHorizontal(
                    dX,
                    isClamped,
                    isCurrentlyActive
                )  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

                if (newX == -clamp) {
                    getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                    return
                }

                currentDx = newX
                getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    view,
                    newX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }



            Log.d("TAG", "onChildDraw(): $actionState")
        }

        //사용자가 뷰를 스와이프 했다고 간주할 최소 속도 정하기
        override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
            Log.d("TAG", "getSwipeEscapeVelocity()")
            return defaultValue * 15
        }


        //사용자가 손을 떼면 호출됨
        //사용자가 뷰를 스와이프했다고 간주하기 위해 이동해야하는 부분 반환
        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            Log.d("TAG", "getSwipeThreshold()")
            setTag(viewHolder, currentDx <= -clamp)
            return 2f
        }

        private fun getView(viewHolder: RecyclerView.ViewHolder): View =
            viewHolder.itemView.findViewById(R.id.swipe_view)

        private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
            viewHolder.itemView.tag = isClamped
        }

        private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean =
            viewHolder.itemView.tag as? Boolean ?: false

        private fun clampViewPositionHorizontal(
            dX: Float,
            isClamped: Boolean,
            isCurrentlyActive: Boolean
        ): Float {
            // RIGHT 방향으로 swipe 막기
            val max = 0f

            // 고정할 수 있으면
            val newX = if (isClamped) {
                // 현재 swipe 중이면 swipe되는 영역 제한
                if (isCurrentlyActive)
                // 오른쪽 swipe일 때
                    if (dX < 0) dX / 3 - clamp
                    // 왼쪽 swipe일 때
                    else dX - clamp
                // swipe 중이 아니면 고정시키기
                else -clamp
            }
            // 고정할 수 없으면 newX는 스와이프한 만큼
            else dX / 2

            // newX가 0보다 작은지 확인
            return min(newX, max)
        }

        fun removePreviousClamp(recyclerView: RecyclerView) {
            // 현재 선택한 view가 이전에 선택한 view와 같으면 패스
            if (currentPosition == previousPosition) return

            // 이전에 선택한 위치의 view 고정 해제
            previousPosition?.let {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
                getView(viewHolder).animate().translationX(0f).setDuration(100L).start()
                setTag(viewHolder, false)
                previousPosition = null
            }

        }

        fun setClamp(clamp: Float) {
            this.clamp = clamp
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantQueryRegisterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO {매장 정보 (메뉴, 이름) 불러오기 }


//        CommonFunc.showToast(requireContext(), args.regNum)
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

                var leftBtnText = ""
                var rightBtnText = ""

                when (currentPage) {
                    0 -> { //개요 설명
                        leftBtnText = "취소"
                        rightBtnText = "만들러가기"

//                        val titleOfZero =
//                            binding.viewPager2[0].findViewById<TextView?>(R.id.tv_title)
//
//                        titleOfZero?.viewTreeObserver?.addOnGlobalLayoutListener(object :
//                            ViewTreeObserver.OnGlobalLayoutListener {
//                            override fun onGlobalLayout() {
//                                if (currentPage == 0) {
//                                    createBalloon("화면을 터치해보세요 !", titleOfZero)
//                                }
//
//                                titleOfZero.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                            }
//
//                        })


                    }

                    1 -> { //매장관련 빠른질문 추가
                        leftBtnText = "이전"
                        rightBtnText = "($currentPage / 3) 다음"


                    }

                    2 -> { //개발메뉴 관련 빠른질문 추가
                        leftBtnText = "이전"
                        rightBtnText = "($currentPage / 3) 다음"


                    }
                    3 -> { //사장님 직접 질문 추가
                        leftBtnText = "이전"
                        rightBtnText = "작성 완료"
                    }


                    else -> { //작성 완료
//                        CommonFunc.showToast(requireContext(), "테스트")


                    }
                }

                when (currentPage) {
                    1, 2, 3 -> {
                        if (!binding.bottomSheet.isVisible) {
                            binding.bottomSheet.visibility = View.VISIBLE
                            binding.bottomSheet.startAnimation(
                                AnimationUtils.loadAnimation(
                                    requireContext(),
                                    R.anim.sticking_on_top
                                )
                            )

                        }
                    }
                    else -> {
                        if (binding.bottomSheet.isVisible) {
                            binding.bottomSheet.startAnimation(
                                AnimationUtils.loadAnimation(
                                    requireContext(),
                                    R.anim.disapearing_on_downward
                                )
                            )
                            binding.bottomSheet.visibility = View.GONE
                        }
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

        binding.bottomSheet.let { bottomSheet ->
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_SETTLING -> {

                            bottomSheet.animate()
                                .translationY(0f)
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            bottomSheet.animate()
                                .setDuration(200)
                                .translationY(40f)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            bottomSheet.animate()
                                .setDuration(200)
                                .translationY(-40f)
                        }
                        else -> Unit
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.rvQuestionnaire.alpha = min(slideOffset * 1.1f, 1f)
                    binding.tvTitleQuestionnaire.alpha = min(slideOffset * 1.1f, 1f)

                }

            })
        }


        binding.tvRight.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (binding.viewPager2.currentItem == 3) {

                    val isSuccessful = registerQuestionnaire()

//                    CommonFunc.showToast(requireContext(), args.regNum)

                    if (!isSuccessful) {

                        CommonFunc.showToast(requireContext(), "질문지를 등록할 수 없습니다")

                        return@launch
                    } else {

                        binding.viewPager2.currentItem += 1


                    }
                } else {

                    binding.viewPager2.currentItem += 1


                }

            }


        }
        binding.tvLeft.setOnClickListener {
            if (binding.viewPager2.currentItem > 0) {
                binding.viewPager2.currentItem -= 1
            } else {
                exitThisFragment()
            }

        }

        binding.vBar.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    BottomSheetBehavior.STATE_HALF_EXPANDED
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    BottomSheetBehavior.STATE_COLLAPSED
                }
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    BottomSheetBehavior.STATE_EXPANDED
                }
                else -> return@setOnClickListener
            }


        }
        binding.rvQuestionnaire.apply {
            questionnaireAdapter = QuestionnaireAdapter()

            questionnaireAdapter.setOnItemRemoveListener { queryLine ->
                viewModel.removeQuery(queryLine)

                val balloon = Balloon.Builder(requireContext())
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.balloon_blue)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setTextTypeface(Typeface.SANS_SERIF)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .setLayout(R.layout.balloon_questionnaire_removed)
                    .build()

                val btnUndo: TextView = balloon.getContentView().findViewById(R.id.tv_undo)

                btnUndo.setOnClickListener {
                    viewModel.insertQuery(queryLine)
                    balloon.dismiss()
                }

                balloon.showAlignTop(binding.rvQuestionnaire)
                balloon.dismissWithDelay(3000L)

            }

            questionnaireAdapter.setOnItemModifyListener { queryLine, view ->
                modifyQueryLine(queryLine, view)
            }
            adapter = questionnaireAdapter
//            layoutManager = LinearLayoutManager(requireContext())

            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.SPACE_BETWEEN
            }

            val swipeHelperCallback = SwipeHelperCallback(questionnaireAdapter).apply {
                setClamp(resources.displayMetrics.widthPixels.toFloat() / 5 * 2 - 30)
            }

            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(this)

            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(binding.rvQuestionnaire)
                false
            }


        }




        viewModel.currentQuestionnaire.observe(viewLifecycleOwner) {
            questionnaireAdapter.submitList(it)

            binding.tvTitleQuestionnaire.text = "지금까지 작성한 질문지 (${it.size} / 8)"

        }

        viewModel.loadCurrentQuestionnaire(reg_num = args.regNum)


    }

    private suspend fun registerQuestionnaire(): Boolean {
        questionnaireAdapter.currentList.let { queryLineList ->

            queryLineList.map { queryLine ->
                queryLine.reg_num = args.regNum
            }


            val result = viewModel.registerQuestionnaire(queryLineList)

            return result.isSuccessful

        }
    }

    private fun modifyQueryLine(queryLine: QueryLine, view: View): Boolean {

        when (queryLine.queryType) {
            QueryLine.QueryType.TypeRestaurant,
            QueryLine.QueryType.TypeMenu -> {
                val balloon = Balloon.Builder(requireContext())
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.point_red)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setTextTypeface(Typeface.SANS_SERIF)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .setLayout(R.layout.balloon_questionnaire_is_already_existed)
                    .build()

                val tvExplains: TextView = balloon.getContentView().findViewById(R.id.tv_explains)

                tvExplains.text = "제공되는 질문은 수정할 수 없어요"

                balloon.showAlignTop(binding.rvQuestionnaire)
                balloon.dismissWithDelay(2000L)
            }
            QueryLine.QueryType.TypeAdd -> {

                if (binding.viewPager2.currentItem != 3) {
                    val balloon = Balloon.Builder(requireContext())
                        .setWidth(BalloonSizeSpec.WRAP)
                        .setHeight(BalloonSizeSpec.WRAP)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowSize(10)
                        .setArrowPosition(0.5f)
                        .setPadding(12)
                        .setCornerRadius(8f)
                        .setBackgroundColorResource(R.color.point_red)
                        .setBalloonAnimation(BalloonAnimation.ELASTIC)
                        .setTextTypeface(Typeface.SANS_SERIF)
                        .setLifecycleOwner(viewLifecycleOwner)
                        .setLayout(R.layout.balloon_questionnaire_is_already_existed)
                        .build()

                    val tvExplains: TextView =
                        balloon.getContentView().findViewById(R.id.tv_explains)

                    tvExplains.text = "수정을 위해 3페이지로 이동해주세요"

                    balloon.showAlignTop(binding.rvQuestionnaire)
                    balloon.dismissWithDelay(2000L)
                } else {

                    val addFragment =
                        fragmentList[3] as RestaurantQueryRegisterAddPersonallyFragment

                    addFragment.modifyQueryLine(queryLine)
                    viewModel.removeQuery(queryLine)
                    return true
                }

            }

        }
        return false
    }

    fun exitThisFragment() {
        findNavController().popBackStack()
    }

    fun addQueryLine(queryLine: QueryLine): Int {
        queryLine.reg_num = args.regNum

        val result = viewModel.insertQuery(queryLine)

        when (result) { //성공시 멘트 보여줌
            IS_SUCCESS -> {
                binding.rvQuestionnaire.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val balloon = Balloon.Builder(requireContext())
                            .setWidth(BalloonSizeSpec.WRAP)
                            .setHeight(BalloonSizeSpec.WRAP)
                            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                            .setArrowSize(10)
                            .setArrowPosition(0.5f)
                            .setPadding(12)
                            .setLayout(R.layout.balloon_questionnaire_added)
                            .setCornerRadius(8f)
                            .setBackgroundColorResource(R.color.balloon_blue)
                            .setBalloonAnimation(BalloonAnimation.ELASTIC)
                            .setTextTypeface(Typeface.SANS_SERIF)
                            .setLifecycleOwner(viewLifecycleOwner)
                            .build()
                        balloon.showAlignTop(binding.bottomSheet)
                        balloon.dismissWithDelay(1000)

                        binding.bottomSheet.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
            IS_ALREADY_EXISTED -> {
                val balloon = Balloon.Builder(requireContext())
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setLayout(R.layout.balloon_questionnaire_is_already_existed)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.point_red)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setTextTypeface(Typeface.SANS_SERIF)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .build()
                balloon.showAlignTop(binding.bottomSheet)

            }
            IS_TOO_MUCH_COUNT -> {
                val balloon = Balloon.Builder(requireContext())
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setLayout(R.layout.balloon_questionnaire_too_much)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.point_red)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setTextTypeface(Typeface.SANS_SERIF)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .build()
                balloon.showAlignTop(binding.bottomSheet)

            }
            else -> Unit


        }

        return result
    }


    fun createBalloon(text: String, view: View = binding.tvRight) {
        val balloon = Balloon.Builder(requireContext())
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText(text)
            .setTextColorResource(R.color.white)
            .setTextSize(20f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setPadding(12)
            .setCornerRadius(4f)
            .setBackgroundColorResource(R.color.balloon_blue)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
        balloon.showAlignTop(view)
    }


}