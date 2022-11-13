package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterNameCorporateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CorporateFragment : Fragment() {

    private var _binding: FragmentRestaurantRegisterNameCorporateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        (parentFragment as RestaurantRegisterFragment).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentRestaurantRegisterNameCorporateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheckRegNumIsUsable.setOnClickListener {
            val inputRegNumber = binding.etCorporateNumber.text.toString()

            if (inputRegNumber.isEmpty()) {
                Toast.makeText(requireContext(), "사업자 등록번호를 입력해주세요", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val result = viewModel.getStoreInfoByRegNum(inputRegNumber)

                if (result.isSuccessful) {
                    val body = result.body()

                    val response = body?.get(0)

                    response?.market?.let {

                        binding.tvCheckResult.text = "해당 사업자 등록번호는 사용하실 수 없습니다"
                        (parentFragment as RestaurantRegisterFragment).setRegNumIsOk(false)

                    } ?: run {
                        binding.tvCheckResult.text = "해당 사업자 등록번호를 사용하실 수 있습니다"
                        (parentFragment as RestaurantRegisterFragment).setRegNumIsOk(true)
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