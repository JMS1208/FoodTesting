package com.capstone.foodtesting.ui.restaurant.review

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.statistics.Age
import com.capstone.foodtesting.data.model.statistics.Gender
import com.capstone.foodtesting.data.model.statistics.PerMonth
import com.capstone.foodtesting.data.model.statistics.ReviewStatistics
import com.capstone.foodtesting.databinding.FragmentReviewAnalBinding
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.util.*
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import java.util.Collections.max
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ReviewAnalFragment : Fragment() {

    private var _binding: FragmentReviewAnalBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ReviewAnalViewModel>()

    private val args by navArgs<ReviewAnalFragmentArgs>()

    private lateinit var simpleReviewAdapter: SimpleReviewAdapter

    private lateinit var reviewByCustomerAdapter: ReviewByCustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewAnalBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbarView()

        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.getReviewStatistics(args.regNum)

            if (response.isSuccessful) {
                response.body()?.reviewStatistics?.let { reviewStatistics ->
                    val total = reviewStatistics.total

                    if(total == 0) {
                        CommonFunc.showToast(requireContext(), "아직 등록된 리뷰가 없습니다")
                        findNavController().popBackStack()
                    }

                    reviewStatistics.age?.let {
                        initAgeBarChart(it, total)
                        var cnt = 0

                        cnt += it.age_10?:0
                        cnt += it.age_20?:0
                        cnt += it.age_30?:0
                        cnt += it.age_40?:0
                        cnt += it.age_50?:0


                        binding.tvCumulativeReview.text = "누적리뷰: ${cnt}개"
                    }
                    reviewStatistics.gender?.let {
                        initGenderPieChart(it, total)
                    }
                    reviewStatistics.perMonth?.let {
                        initMonthLineChart(it, total)
                    }
                    setupStatisticsResult(reviewStatistics)

                }
            } else {
                CommonFunc.showToast(requireContext(), "네트워크 연결에 문제가 있습니다")
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.getRestaurantInfoByRegNum(args.regNum)

            if (response.isSuccessful) {
                response.body()?.get(0)?.market?.let {
                    binding.tvRestaurantName.text = "${it.name} [${it.category}]"
                    Glide.with(requireContext())
                        .load(it.photoUrl)
                        .into(binding.ivRestaurantImage)
                } ?: run {
                    CommonFunc.showToast(requireContext(), "매장 조회가 되지 않습니다")
                    findNavController().popBackStack()
                }

            } else {
                CommonFunc.showToast(requireContext(), "네트워크 연결에 문제가 있습니다")
                findNavController().popBackStack()
            }
        }


        setupSimpleRecyclerview()
        setupScrollChangeListener()
        setupReviewByCustomerRecyclerview()
        setupReviewSummary()
        setupLottieView()
    }

    private fun setupLottieView() {

        binding.lvRobot.apply {
            sandboxAnimations()
            playAnimation()
        }
    }

    private fun setupReviewSummary() {
        binding.llReviewSummary.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val dialog = DialogLoading()
                try {
                    dialog.setMessage("리뷰를 요약하고 있습니다...\n1~2분 정도 소요됩니다")
                    dialog.setOutsideCancellable(false)
                    dialog.isCancelable = false
                    dialog.show(childFragmentManager, dialog.tag)

                    val response =
                        viewModel.getReviewSummary(args.regNum)


                    val jobResult = viewLifecycleOwner.lifecycleScope.async {
                        if (response.isSuccessful) {
                            response.body()?.message ?: "리뷰 요약에 실패하였습니다"
                        } else {
                            "리뷰 요약에 실패하였습니다"
                        }
                    }

                    if (dialog.isVisible) {
                        dialog.dismiss()
                    }
                    val summary = jobResult.await()

                    if(summary == "리뷰 요약에 실패하였습니다") {
                        showToast(requireContext(), summary)
                    } else {
                        val summaryDialog = DialogReviewSummary()

                        summaryDialog.setMessage(summary.trim())
                        summaryDialog.show(childFragmentManager, summaryDialog.tag)
                    }

                } catch(E: Exception) {

                    if (dialog.isVisible) {
                        dialog.dismiss()
                    }

                    showToast(requireContext(), "${E.message}")
                }



            }
        }
    }

    private fun setupReviewByCustomerRecyclerview() {
        reviewByCustomerAdapter = ReviewByCustomerAdapter()

        binding.rvReview.apply {
            adapter = reviewByCustomerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.getReviewsForRestaurant(args.regNum)

            if(response.isSuccessful) {
                response.body()?.reviewAll?.let {
                    reviewByCustomerAdapter.submitList(it)
                }
            }
        }

    }

    private fun initToolbarView() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupStatisticsResult(reviewStatistics: ReviewStatistics) {

        try {
            val ages = ArrayList<Int>()

            ages.apply {
                add(reviewStatistics.age!!.age_10!!)
                add(reviewStatistics.age.age_20!!)
                add(reviewStatistics.age.age_30!!)
                add(reviewStatistics.age.age_40!!)
                add(reviewStatistics.age.age_50!!)
            }

            val maxAge = max(ages)

            val maxAgeIdx = ArrayList<Int>()

            for (i in 0 until ages.size) {
                if (ages[i] == maxAge) {
                    maxAgeIdx.add(i)
                }
            }

            var maxAgeText = ""

            for (i in 0 until maxAgeIdx.size) {
                when (maxAgeIdx[i]) {
                    0 -> { // 10대
                        maxAgeText += " 10대"
                    }
                    1 -> { // 20대
                        maxAgeText += " 20대"
                    }

                    2 -> { //30대
                        maxAgeText += " 30대"
                    }
                    3 -> { //40대
                        maxAgeText += " 40대"
                    }
                    4 -> { //50대
                        maxAgeText += " 50대 이상"
                    }
                    else -> Unit
                }
            }

            var maxGenderText = ""

            reviewStatistics.gender?.let {
                maxGenderText += if (it.male!! < it.female!!) {
                    "여성"
                } else if (it.male > it.female) {
                    "남성"
                } else {
                    ""
                }
            }


            binding.tvResultAgeGender.text = "주 손님층은 \'$maxGenderText $maxAgeText\' 입니다"


            reviewStatistics.perMonth?.let {
                val months = ArrayList<Int>()

                months.apply {
                    add(it.month_1!!)
                    add(it.month_2!!)
                    add(it.month_3!!)
                    add(it.month_4!!)
                    add(it.month_5!!)
                    add(it.month_6!!)
                    add(it.month_7!!)
                    add(it.month_8!!)
                    add(it.month_9!!)
                    add(it.month_10!!)
                    add(it.month_11!!)
                    add(it.month_12!!)
                }

                val stack = Stack<Pair<Int, Int>>()

                for (i in 0 until months.size) {

                    while (stack.isNotEmpty() && months[i] > stack.peek().second) {
                        stack.pop()
                    }

                    if (months[i] > 0) {
                        stack.add(Pair(i, months[i]))
                    }


                }

                var maxMonthText = ""


                val arr = stack.toList()

                arr.forEach {
                    maxMonthText += " ${it.first + 1}월"
                }

                if (maxMonthText.length > 1) {
                    binding.tvResultMonth.text = "가장 손님이 많았던 달은 \'${maxMonthText.trim()}\'이었어요"

                }


            }


        } catch (E: Exception) {
            Log.e("TAG", "setupStatisticsResult() 오류: ${E.printStackTrace()}")
        }

    }

    private fun setupSimpleRecyclerview() {
//        simpleReviewAdapter = SimpleReviewAdapter().apply {
//            setOnItemClickListener {
//
//            }
//        }
//        binding.rvReview.apply {
//            adapter = simpleReviewAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            //여기서 심플리뷰 가져오기
//        }


    }


    private fun setupScrollChangeListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView.setOnScrollChangeListener { p0, p1, scrollY, p3, p4 ->

                when (scrollY) {
                    0 -> {
                        binding.tabLayout.setScrollPosition(0, 0f, true)
                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.llIdx0
                    )..binding.nestedScrollView.computeDistanceToView(binding.llIdx1)
                    -> {
                        binding.tabLayout.setScrollPosition(1, 0f, true)

                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.llIdx1
                    )..binding.nestedScrollView.computeDistanceToView(binding.llIdx2)
                    -> {
                        binding.tabLayout.setScrollPosition(2, 0f, true)

                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.llIdx2
                    )..binding.nestedScrollView.computeDistanceToView(binding.llIdx3)
                    -> {
                        binding.tabLayout.setScrollPosition(3, 0f, true)

                    }
                    in binding.nestedScrollView.computeDistanceToView(
                        binding.llIdx3
                    )..binding.nestedScrollView.computeDistanceToView(binding.vIdx4)
                    -> {
                        binding.tabLayout.setScrollPosition(4, 0f, true)

                    }
                    else -> {
                        binding.tabLayout.setScrollPosition(5, 0f, true)
                    }
                }

                if (!binding.nestedScrollView.canScrollVertically(1)) {
                    binding.tabLayout.setScrollPosition(5, 0f, true)
                }
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> binding.nestedScrollView.scrollToView(binding.vIdx0)
                    1 -> binding.nestedScrollView.scrollToView(binding.vIdx0)
                    2 -> binding.nestedScrollView.scrollToView(binding.vIdx1)
                    3 -> binding.nestedScrollView.scrollToView(binding.vIdx2)
                    4 -> binding.nestedScrollView.scrollToView(binding.vIdx3)
                    5 -> binding.nestedScrollView.scrollToView(binding.vIdx4)
                    else -> Unit
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun androidx.core.widget.NestedScrollView.computeDistanceToView(view: View): Int {

        return (view.y - this.y).toInt()
    }


    private fun initMonthLineChart(perMonth: PerMonth?, total: Int?) {
        //데이터 생산

        try {
            val monthEntries: ArrayList<Entry> = ArrayList()
            monthEntries.apply {
                add(Entry(1f, perMonth!!.month_1!!.toFloat()))
                add(Entry(2f, perMonth.month_2!!.toFloat()))
                add(Entry(3f, perMonth.month_3!!.toFloat()))
                add(Entry(4f, perMonth.month_4!!.toFloat()))
                add(Entry(5f, perMonth.month_5!!.toFloat()))
                add(Entry(6f, perMonth.month_6!!.toFloat()))
                add(Entry(7f, perMonth.month_7!!.toFloat()))
                add(Entry(8f, perMonth.month_8!!.toFloat()))
                add(Entry(9f, perMonth.month_9!!.toFloat()))
                add(Entry(10f, perMonth.month_10!!.toFloat()))
                add(Entry(11f, perMonth.month_11!!.toFloat()))
                add(Entry(12f, perMonth.month_12!!.toFloat()))
            }
            setupLineChart(binding.chartMonth)
            loadLineChartData(binding.chartMonth, monthEntries)
        } catch (E: Exception) {
            Log.e("TAG", "initMonthLineChart() 오류: ${E.printStackTrace()}")
        }


    }

    private fun setupLineChart(lineChart: LineChart) {
        lineChart.apply {
            description.text = "단위: 명"
            description.textSize = 14f
            xAxis.apply {
//                isEnabled = false
                valueFormatter = MonthValueFormatter()
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setLabelCount(12, false)
                axisLineWidth = 1f
            }

            axisRight.apply {
                isEnabled = false

            }
            axisLeft.apply {
                isEnabled = false
            }
            legend.apply {
                isEnabled = false
            }
        }
    }

    private fun loadLineChartData(
        lineChart: LineChart,
        dataEntries: ArrayList<Entry>,
        customColors: ArrayList<Int> = ArrayList()
    ) {
        val colors: ArrayList<Int> = ArrayList()

        customColors.forEach {
            colors.add(it)
        }

        for (color in ColorTemplate.PASTEL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.JOYFUL_COLORS) {
            colors.add(color)
        }

        val dataSet = LineDataSet(dataEntries, "")
        dataSet.apply {
            this.colors = colors
            this.valueFormatter = MyValueFormatter()
        }

        val data = LineData(dataSet)
        data.apply {
            setDrawValues(true)
            setValueFormatter(MyValueFormatter())
            setValueTextSize(12f)
            setValueTextColor(Color.BLACK)
        }

        lineChart.apply {
            this.data = data
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            invalidate()
            animateX(1400, Easing.EaseInOutQuad)
        }
    }

    private fun initAgeBarChart(age: Age?, total: Int?) {

        try {
            val ageEntries: ArrayList<BarEntry> = ArrayList()
            ageEntries.apply {
                //10대, 20대, 30대, 40대, 50대 이상
                add(BarEntry(10.0f, age!!.age_10!!.toFloat()))
                add(BarEntry(20.0f, age.age_20!!.toFloat()))
                add(BarEntry(30.0f, age.age_30!!.toFloat()))
                add(BarEntry(40.0f, age.age_40!!.toFloat()))
                add(BarEntry(50.0f, age.age_50!!.toFloat()))
            }
            setupBarChart(binding.chartAge)
            loadBarChartData(binding.chartAge, ageEntries)
        } catch (E: Exception) {
            Log.e("TAG", "initAgeBarChart 오류 ${E.printStackTrace()}")
        }

    }

    private fun setupBarChart(barChart: BarChart) {
        barChart.apply {

            description.isEnabled = false
            legend.isEnabled = false
            setDrawValueAboveBar(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            isDoubleTapToZoomEnabled = false
            renderer = CustomBarChartRender(this, this.animator, this.viewPortHandler).apply {
                setRadius(20)
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawLabels(true)
                valueFormatter = AgeValueFormatter()
                granularity = 10f
                setLabelCount(5, false)
            }
            axisLeft.apply {
                isEnabled = false
            }
            axisRight.apply {
                isEnabled = false
            }


        }
    }

    private fun loadBarChartData(
        barChart: BarChart,
        dataEntries: ArrayList<BarEntry>,
        customColors: ArrayList<Int> = ArrayList()
    ) {


        val colors: ArrayList<Int> = ArrayList()


        customColors.forEach {
            colors.add(it)
        }


        for (color in ColorTemplate.PASTEL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.JOYFUL_COLORS) {
            colors.add(color)
        }

        val dataSet = BarDataSet(dataEntries, "")
        dataSet.apply {
            this.colors = colors
            this.valueFormatter = MyValueFormatter("명")
        }

        val data = BarData(dataSet)
        data.apply {
            setDrawValues(true)
            setValueFormatter(MyValueFormatter("명"))
            setValueTextSize(12f)
            setValueTextColor(Color.BLACK)
            barWidth = 5f
        }

        barChart.apply {
            this.data = data
            setDrawBarShadow(false)
            setFitBars(true)
            invalidate()
            animateY(1400, Easing.EaseInOutQuad)

        }

    }

    private fun initGenderPieChart(gender: Gender?, total: Int?) {

        try {
            val genderEntries: ArrayList<PieEntry> = ArrayList()
            genderEntries.apply {
                add(PieEntry(gender!!.male!!.div(total!!.toFloat()), "남성"))
                add(PieEntry(gender.unknown!!.div(total.toFloat()), "미상"))
                add(PieEntry(gender.female!!.div(total.toFloat()), "여성"))
            }

            val colors: ArrayList<Int> = ArrayList()

            colors.apply {
                add(ContextCompat.getColor(requireContext(), R.color.gradient_color1))
                add(ContextCompat.getColor(requireContext(), R.color.gradient_color2))
                add(ContextCompat.getColor(requireContext(), R.color.gradient_color3))
            }

            setupPieChart(binding.chartGender, "성별")
            loadPieChartData(binding.chartGender, genderEntries, colors)

        } catch (E: Exception) {
            Log.e("TAG", "initGenderPieChart 오류 ${E.printStackTrace()}")
        }


    }

    private fun setupPieChart(pieChart: PieChart, centerText: String) {

        pieChart.apply {
            extraBottomOffset = 10f
            extraTopOffset = 10f
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.WHITE)
            this.centerText = centerText
            setCenterTextSize(13f)
            description.isEnabled = false
            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                isEnabled = true
            }

        }

    }

    private fun loadPieChartData(
        pieChart: PieChart,
        dataEntries: ArrayList<PieEntry>,
        customColors: ArrayList<Int> = ArrayList()
    ) {


        val colors: ArrayList<Int> = ArrayList()


        customColors.forEach {
            colors.add(it)
        }


        for (color in ColorTemplate.PASTEL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.JOYFUL_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(dataEntries, "")
        dataSet.apply {
            this.colors = colors
            this.sliceSpace = 3f
            selectionShift = 5f
            valueLinePart1Length = 0.4f
            valueLinePart2Length = 0.8f
            valueLinePart1OffsetPercentage = 80f
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        }

        val data = PieData(dataSet)
        data.apply {
            setDrawValues(true)
            setValueFormatter(PercentFormatter(binding.chartGender))
            setValueTextSize(12f)
            setValueTextColor(Color.BLACK)
        }

        pieChart.apply {
            this.data = data
            invalidate()
            animateY(1400, Easing.EaseInOutQuad)

        }

    }
}
