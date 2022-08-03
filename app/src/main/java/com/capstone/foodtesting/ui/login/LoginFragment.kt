package com.capstone.foodtesting.ui.login

import android.opengl.Visibility
import android.os.Build.VERSION_CODES.P
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

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
        setupBottomNavigationViewVisible(false)

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
    }

    private fun setupBottomNavigationViewVisible(boolean: Boolean) {

        val bottomNavView: BottomNavigationView? = activity?.findViewById(R.id.bottomNavigationView)

        bottomNavView?.apply {
            visibility = if(boolean) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        setupBottomNavigationViewVisible(true)
        _binding = null
    }

}