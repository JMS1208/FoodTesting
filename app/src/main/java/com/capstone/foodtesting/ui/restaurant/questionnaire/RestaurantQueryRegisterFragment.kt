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

        // swipe_view ??? swipe ?????? ??? <??????> ????????? ???????????? ???????????? ?????? ?????????
        private var currentPosition: Int? = null    // ?????? ????????? recycler view??? position
        private var previousPosition: Int? = null   // ????????? ???????????? recycler view??? position
        private var currentDx = 0f                  // ?????? x ???
        private var clamp = 0f                      // ???????????? ??????
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
            when (direction) { //????????? ?????? ?????? ???????????????
                LEFT -> {

                }
                RIGHT -> {

                }
                else -> return
            }

            Log.d("TAG", "onSwiped()")
        }

        //??????????????? ??????????????? ?????????????????? ????????? ??? ??????
        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            currentDx = 0f
            previousPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().clearView(getView(viewHolder))

            Log.d("TAG", "clearView() ")
        }


        //???????????? ???????????? ???????????? ????????? ???????????? ??????
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            viewHolder?.let {
                currentPosition = viewHolder.absoluteAdapterPosition
                getDefaultUIUtil().onSelected(getView(it))

            }
            Log.d("TAG", "onSelectedChanged() ")
        }

        //???????????? ??????????????? ?????????????????? ??? ?????? ????????? ?????? ?????? ??????
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
                )  // newX ?????? ??????(?????? ??? ?????? ??????/?????? ?????? ??? ?????? ?????? ??????)

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

        //???????????? ?????? ???????????? ????????? ????????? ?????? ?????? ?????????
        override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
            Log.d("TAG", "getSwipeEscapeVelocity()")
            return defaultValue * 15
        }


        //???????????? ?????? ?????? ?????????
        //???????????? ?????? ????????????????????? ???????????? ?????? ?????????????????? ?????? ??????
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
            // RIGHT ???????????? swipe ??????
            val max = 0f

            // ????????? ??? ?????????
            val newX = if (isClamped) {
                // ?????? swipe ????????? swipe?????? ?????? ??????
                if (isCurrentlyActive)
                // ????????? swipe??? ???
                    if (dX < 0) dX / 3 - clamp
                    // ?????? swipe??? ???
                    else dX - clamp
                // swipe ?????? ????????? ???????????????
                else -clamp
            }
            // ????????? ??? ????????? newX??? ??????????????? ??????
            else dX / 2

            // newX??? 0?????? ????????? ??????
            return min(newX, max)
        }

        fun removePreviousClamp(recyclerView: RecyclerView) {
            // ?????? ????????? view??? ????????? ????????? view??? ????????? ??????
            if (currentPosition == previousPosition) return

            // ????????? ????????? ????????? view ?????? ??????
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

        //TODO {?????? ?????? (??????, ??????) ???????????? }


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
                    0 -> { //?????? ??????
                        leftBtnText = "??????"
                        rightBtnText = "???????????????"

//                        val titleOfZero =
//                            binding.viewPager2[0].findViewById<TextView?>(R.id.tv_title)
//
//                        titleOfZero?.viewTreeObserver?.addOnGlobalLayoutListener(object :
//                            ViewTreeObserver.OnGlobalLayoutListener {
//                            override fun onGlobalLayout() {
//                                if (currentPage == 0) {
//                                    createBalloon("????????? ?????????????????? !", titleOfZero)
//                                }
//
//                                titleOfZero.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                            }
//
//                        })


                    }

                    1 -> { //???????????? ???????????? ??????
                        leftBtnText = "??????"
                        rightBtnText = "($currentPage / 3) ??????"


                    }

                    2 -> { //???????????? ?????? ???????????? ??????
                        leftBtnText = "??????"
                        rightBtnText = "($currentPage / 3) ??????"


                    }
                    3 -> { //????????? ?????? ?????? ??????
                        leftBtnText = "??????"
                        rightBtnText = "?????? ??????"
                    }


                    else -> { //?????? ??????
                        CommonFunc.showToast(requireContext(), "?????????")


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

                        CommonFunc.showToast(requireContext(), "???????????? ????????? ??? ????????????")

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

            binding.tvTitleQuestionnaire.text = "???????????? ????????? ????????? (${it.size} / 8)"

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

                tvExplains.text = "???????????? ????????? ????????? ??? ?????????"

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

                    tvExplains.text = "????????? ?????? 3???????????? ??????????????????"

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

        when (result) { //????????? ?????? ?????????
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