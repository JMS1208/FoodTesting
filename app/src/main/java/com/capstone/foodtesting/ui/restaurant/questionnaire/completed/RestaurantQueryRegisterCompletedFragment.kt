package com.capstone.foodtesting.ui.restaurant.questionnaire.completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.foodtesting.databinding.FragmentRestaurantQueryRegisterCompletedBinding
import com.capstone.foodtesting.ui.restaurant.questionnaire.RestaurantQueryRegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantQueryRegisterCompletedFragment : Fragment() {

    private var _binding : FragmentRestaurantQueryRegisterCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantQueryRegisterCompletedBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMoveHome.setOnClickListener {
            (parentFragment as RestaurantQueryRegisterFragment).exitThisFragment()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}