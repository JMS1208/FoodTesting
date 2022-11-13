package com.capstone.foodtesting.ui.member.review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.databinding.FragmentMemberReviewContentsBinding


class MemberReviewContentsFragment : Fragment() {

    private var _binding: FragmentMemberReviewContentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var keywordAdapter: KeywordAdapter

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


    }

    private fun setupKeywordAdapter() {
        keywordAdapter = KeywordAdapter()
        keywordAdapter.setOnItemClickListener { keyword ->
//            val currentCursor = binding.etAnswer.selectionStart
//            binding.etAnswer.append(keyword, currentCursor, currentCursor+keyword.length-1)
        }
        keywordAdapter.setChipCloseBtnEnabled(false)
        binding.rvKeyword.apply {
            adapter = keywordAdapter
            layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        }
    }

    fun setQueryLineInFragment(queryLine: QueryLine) {
        binding.tvQuery.text = queryLine.query
        keywordAdapter.submitList(queryLine.keywords)
    }

    fun saveAnswer(position: Int) {
        val answer = binding.etAnswer.text.toString()

        (parentFragment as MemberReviewFragment).answerList[position].contents = answer
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}