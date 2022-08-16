package com.capstone.foodtesting.ui.login

import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R

import com.capstone.foodtesting.databinding.FragmentLoginBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionFragmentLoginToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener { //테스트용
            //아래는 홈 화면 들어가는 코드
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
            findNavController().navigate(action)

            //아래는 글쓰기 화면 들어가는 코드
            //findNavController().navigate(R.id.fragment_write)
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}