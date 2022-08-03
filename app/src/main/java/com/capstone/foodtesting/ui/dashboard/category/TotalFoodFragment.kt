package com.capstone.foodtesting.ui.dashboard.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentKoreanFoodBinding
import com.capstone.foodtesting.databinding.FragmentTotalFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalFoodFragment : Fragment() {

    private var _binding: FragmentTotalFoodBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalFoodBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}