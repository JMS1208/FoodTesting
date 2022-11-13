package com.capstone.foodtesting.ui.member.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.databinding.FragmentMemberSignUpCompletedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberSignUpCompletedFragment : Fragment() {

    private var _binding: FragmentMemberSignUpCompletedBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MemberSignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberSignUpCompletedBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            setFragmentResult("SignUpCompleted", Bundle().apply {
                putBoolean("login",true)
            })
            findNavController().popBackStack()
        }
        binding.btnNotLogin.setOnClickListener {
            setFragmentResult("SignUpCompleted", Bundle().apply {
                putBoolean("login",false)
            })
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}