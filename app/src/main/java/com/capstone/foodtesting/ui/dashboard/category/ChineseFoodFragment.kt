package com.capstone.foodtesting.ui.dashboard.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentChineseFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChineseFoodFragment : Fragment() {

    private var _binding: FragmentChineseFoodBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChineseFoodBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



}