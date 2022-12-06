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
import com.capstone.foodtesting.ui.activity.MainActivity
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

        addTextChangeListenerToETAnswer()




        setActivityDispatchTouchAvailable(false)
    }

    private fun addTextChangeListenerToETAnswer() {
        binding.etAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(answer: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentPage = (parentFragment as MemberReviewFragment).getCurrentPage()
                viewModel.userReviewList.value?.apply {
                    this[currentPage].contents = answer.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })
    }

    private fun setActivityDispatchTouchAvailable(isAvailable: Boolean) {
        (requireActivity() as MainActivity).setDispatchTouchAvailable(isAvailable)
    }

    private fun setupKeywordAdapter() {
        keywordAdapter = KeywordAdapter()
        keywordAdapter.setOnItemClickListener { keyword ->

            val currIdx = binding.etAnswer.selectionEnd

            val oriText = binding.etAnswer.text.toString()

            binding.etAnswer.text.clear()

            when (currIdx) {
                0 -> {

                    val newText = "${keyword.trim()} $oriText"
                    binding.etAnswer.setText(newText)
                    binding.etAnswer.setSelection(newText.lastIndex+1)

                }
                oriText.lastIndex + 1 -> {

                    val newText = "$oriText ${keyword.trim()}"
                    binding.etAnswer.setText(newText)
                    binding.etAnswer.setSelection(newText.lastIndex+1)

                }
                else -> {

                    val beforeText = oriText.substring(0, currIdx)
                    val afterText = oriText.substring(currIdx)
                    val newText =
                        "${beforeText.trimEnd()} ${keyword.trim()} ${afterText.trimStart()}"
                    binding.etAnswer.setText(newText)
                    binding.etAnswer.setSelection("${beforeText.trimEnd()} ${keyword.trim()}".lastIndex+1)
                }
            }


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
        setActivityDispatchTouchAvailable(true)
        super.onDestroyView()
        _binding = null
    }

}