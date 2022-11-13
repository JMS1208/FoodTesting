package com.capstone.foodtesting.ui.common.dashboard.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.foodtesting.databinding.FragmentCommonDashBoardCategoryContentsBinding


class JapaneseFoodFragment : Fragment() {

    private var _binding : FragmentCommonDashBoardCategoryContentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommonDashBoardCategoryContentsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "일식삭제")
        _binding = null
    }

}