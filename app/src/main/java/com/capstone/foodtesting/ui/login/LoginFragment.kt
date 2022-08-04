package com.capstone.foodtesting.ui.login

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
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.databinding.FragmentLoginBinding
import com.capstone.foodtesting.util.Constants.NEED_TO_LOGIN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

        BottomSheetBehavior.from(binding.bottomSheetLogin).apply {
            peekHeight = 0
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.tvLogin.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetLogin).apply {
                this.state =BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.btnStart.setOnClickListener {
            savedInstanceState?.putBoolean(NEED_TO_LOGIN, false)
            val action = LoginFragmentDirections.actionFragmentLoginToFragmentHome()
            findNavController().navigate(action)
        }

        binding.ivLogoLogin.setOnClickListener {
            lifecycleScope.launch {
                when(viewModel.getLogInState()) {
                    LogInStateOptions.LOGGED_IN.value -> {
                        Toast.makeText(requireContext(), "로그아웃 됨", Toast.LENGTH_SHORT).show()
                        viewModel.saveLogInState(LogInStateOptions.LOGGED_OUT.value)
                    }
                    else-> {
                        Toast.makeText(requireContext(),"로그인 됨", Toast.LENGTH_SHORT).show()
                        viewModel.saveLogInState(LogInStateOptions.LOGGED_IN.value)
                    }
                }
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}