package com.capstone.foodtesting.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(needToLogin()) { // 로그인이 필요하면 로그인 화면으로 보냄
            findNavController().navigate(R.id.fragment_login)
        }


    }

    private fun needToLogin(): Boolean { //일단 무조건 로그인하게 만듦
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root
    }


}