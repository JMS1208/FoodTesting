package com.capstone.foodtesting.ui.posting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingFragment : Fragment() {

    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PostingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}