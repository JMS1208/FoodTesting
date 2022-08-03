package com.capstone.foodtesting.ui.dashboard.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentChineseFoodBinding
import com.capstone.foodtesting.databinding.FragmentDessertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DessertFragment : Fragment() {

    private var _binding: FragmentDessertBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDessertBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
}