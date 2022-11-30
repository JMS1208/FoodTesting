package com.capstone.foodtesting.ui.restaurant.questionnaire.add

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentRestaurantQueryRegisterAddPersonallyBinding
import com.capstone.foodtesting.ui.member.review.KeywordAdapter
import com.capstone.foodtesting.ui.restaurant.questionnaire.RestaurantQueryRegisterFragment
import com.capstone.foodtesting.util.CommonFunc.showTooltip
import com.capstone.foodtesting.util.Constants.IS_ALREADY_EXISTED
import com.capstone.foodtesting.util.Constants.IS_SUCCESS
import com.capstone.foodtesting.util.Constants.IS_TOO_MUCH_COUNT
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantQueryRegisterAddPersonallyFragment : Fragment() {

    private var _binding: FragmentRestaurantQueryRegisterAddPersonallyBinding? = null
    private val binding get() = _binding!!

    private lateinit var keywordAdapter: KeywordAdapter

//    private val currentKeywords = mutableListOf<String>()

    private val viewModel by viewModels<RestaurantQueryRegisterAddPersonallyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantQueryRegisterAddPersonallyBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keywordAdapter = KeywordAdapter()



        binding.rvKeyword.apply {
            adapter = keywordAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
            //layoutManager = FlexboxLayoutManager(requireContext())


        }


        binding.ivExplains.setOnClickListener {
//            showToast(requireContext(),"해당 질문과 같이 제공되며, 질문에 대한 보기의 역할을 합니다", binding.ivExplains, window = requireActivity().window)
            showTooltip(
                requireContext(),
                binding.ivExplains,
                "해당 질문과 같이 제공되며\n질문에 대한 보기 역할을 합니다",
                viewLifecycleOwner
            )
        }

        keywordAdapter.setOnItemRemoveListener { keyword ->

            viewModel.removeKeyword(keyword)


        }


        binding.tvAddKeyword.setOnClickListener {
            val keyword = binding.etKeywordContents.text.toString().trim()
            if (keyword.isEmpty()) {

//                showToast(requireContext(), "키워드를 입력해주세요!")
                showTooltip(
                    requireContext(),
                    binding.tvAddKeyword,
                    "키워드를 입력해주세요!",
                    viewLifecycleOwner
                )
            } else {

                val result = viewModel.insertKeyword(keyword)

                if (result) {
                    if (keywordAdapter.currentList.isNotEmpty()) {
                        binding.rvKeyword.smoothScrollToPosition(keywordAdapter.currentList.lastIndex)
                    }

                    binding.etKeywordContents.text.clear()


                } else {
                    showTooltip(
                        requireContext(),
                        binding.tvAddKeyword,
                        "이미 추가한 키워드입니다!",
                        viewLifecycleOwner
                    )

                }


            }

        }

        binding.btnAddQuery.setOnClickListener {

            val query = binding.etQueryContents.text.toString().trim()

            if (query.isEmpty()) {
                showTooltip(requireContext(),binding.btnAddQuery,"질문을 입력해주세요!" )

            } else {

                val remainedKeyword = binding.etKeywordContents.text.toString().trim()

                if (remainedKeyword.isNotEmpty()) {
                    val balloon = Balloon.Builder(requireContext())
                        .setWidth(BalloonSizeSpec.WRAP)
                        .setHeight(BalloonSizeSpec.WRAP)
                        .setLayout(R.layout.balloon_add_query_notice)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowSize(10)
                        .setArrowPosition(0.5f)
                        .setPadding(12)
                        .setCornerRadius(8f)
                        .setBackgroundColorResource(R.color.balloon_blue)
                        .setBalloonAnimation(BalloonAnimation.ELASTIC)
                        .setTextTypeface(Typeface.SANS_SERIF)
                        .setLifecycleOwner(viewLifecycleOwner)
                        .build()
                    balloon.showAlignTop(binding.btnAddQuery)

                    val btnYes: TextView = balloon.getContentView().findViewById(R.id.tv_yes)
                    val btnNope: TextView = balloon.getContentView().findViewById(R.id.tv_nope)

                    btnYes.setOnClickListener {
                        val keywordList = keywordAdapter.currentList.toMutableList()
                        val newKeyword = binding.etKeywordContents.text.toString()
                        keywordList.add(newKeyword)
                        addQueryLine(query, keywordList)
                        balloon.dismiss()
                    }

                    btnNope.setOnClickListener {
                        val keywordList = keywordAdapter.currentList
                        addQueryLine(query, keywordList)
                        balloon.dismiss()
                    }

                } else {
                    val keywordList = keywordAdapter.currentList

                    addQueryLine(query, keywordList)

                }


            }
        }

        viewModel.currentKeywords.observe(viewLifecycleOwner) {
            keywordAdapter.submitList(it.toMutableList())
            if(it.isNotEmpty()) {
                binding.rvKeyword.smoothScrollToPosition(it.lastIndex)
                if(binding.rvKeyword.measuredHeight == 0) {
                    binding.rvKeyword.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                }
            }
        }

    }

    private fun addQueryLine(query: String, keywordList: List<String>) {
        val regNum = (parentFragment as RestaurantQueryRegisterFragment).getRegNum()
        val queryLine = QueryLine(reg_num = regNum, query = query, keywords = keywordList, queryType = QueryLine.TypeAdd)
        val result = (parentFragment as RestaurantQueryRegisterFragment).addQueryLine(queryLine)

        when (result) {
            IS_SUCCESS -> {
                binding.etQueryContents.text.clear()
                binding.etKeywordContents.text.clear()
                viewModel.clearKeywords()
            }
            IS_ALREADY_EXISTED -> {

            }
            IS_TOO_MUCH_COUNT -> {

            }
            else -> Unit
        }

    }

    fun modifyQueryLine(queryLine: QueryLine) {
        binding.etQueryContents.setText(queryLine.query)
        viewModel.replaceKeywords(queryLine.keywords?.toMutableList() ?: mutableListOf())

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}