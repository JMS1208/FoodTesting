package com.capstone.foodtesting.ui.member.review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.databinding.FragmentMemberReviewContentsBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class MemberReviewContentsFragment : Fragment() {

    private var _binding: FragmentMemberReviewContentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var keywordAdapter: KeywordAdapter

    private var queryLine: QueryLine? = null

    private val viewModel by lazy {
        (parentFragment as MemberReviewFragment).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberReviewContentsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKeywordAdapter()
        initQueryView()

        binding.etAnswer.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(answer: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentPage = (parentFragment as MemberReviewFragment).getCurrentPage()
                viewModel.userReviewList.value?.apply {
                    this[currentPage].contents = answer.toString()
//                    Log.d("TAG", "저장완료")
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

    }

    private fun setupKeywordAdapter() {
        keywordAdapter = KeywordAdapter()
        keywordAdapter.setOnItemClickListener { keyword ->

            val idx = binding.etAnswer.selectionEnd
            val oriText = binding.etAnswer.text.toString()

            val newText = when(idx) {
                0 -> { //맨앞
                    "$keyword $oriText"
                }
                oriText.lastIndex+1-> { //맨뒤
                    "$oriText $keyword"
                }
                else -> { //그외
                    val beforeText = oriText.substring(0, idx)
                    val afterText = oriText.substring(idx)
                    "${beforeText.trimEnd()} $keyword ${afterText.trimStart()}"
                }
            }

//
//
//            val beforeText = oriText.substring(0, idx)
//            val afterText = oriText.substring(idx+1)
//            val keywordAddedText = "$beforeText $keyword $afterText"
//
//
            binding.etAnswer.text.clear()

            binding.etAnswer.setText(newText)

            Log.d("TAG", "현재 Selection: $idx, ${binding.etAnswer.selectionStart}, ${oriText.lastIndex}")

        }
        keywordAdapter.setChipCloseBtnEnabled(false)




        binding.rvKeyword.apply {
            adapter = keywordAdapter
//            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)

            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.SPACE_BETWEEN
            }
        }
    }

    fun setQueryLineInFragment(queryLine: QueryLine) {
        this.queryLine = queryLine

    }

    fun initQueryView() {
        queryLine?.let {
            binding.tvQuery.text = it.query
            keywordAdapter.submitList(it.keywords)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}