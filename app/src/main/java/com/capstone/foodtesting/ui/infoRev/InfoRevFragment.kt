package com.capstone.foodtesting.ui.infoRev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.databinding.FragmentInforevBinding
import com.capstone.foodtesting.ui.info.InfoViewModel

class InfoRevFragment: Fragment(){
    private var _binding: FragmentInforevBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<InfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInforevBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val action=InfoRevFragmentDirections.actionInfoRevFragment2ToFragmentInfo()
            findNavController().navigate(action)
        }

        binding.btnClose.setOnClickListener {
            val action=InfoRevFragmentDirections.actionInfoRevFragment2ToFragmentInfo()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}