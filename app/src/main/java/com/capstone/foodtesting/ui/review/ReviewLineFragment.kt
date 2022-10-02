package com.capstone.foodtesting.ui.review

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.foodtesting.databinding.FragmentReviewLineBinding


class ReviewLineFragment : Fragment() {

    private var _binding: FragmentReviewLineBinding? = null
    private val binding get() = _binding!!

    private lateinit var keywordAdapter: KeywordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewLineBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val tmpList = mutableListOf<String>()

        for (i in 0 until 30) {
            tmpList.add("아무말${i+1}")
        }

        keywordAdapter.submitList(tmpList)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}