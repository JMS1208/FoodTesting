package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterNameCorporateBinding
import com.capstone.foodtesting.util.CommonFunc
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        binding.etCorporateNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputText?.let {
                    if (it.length == 10) {
                        binding.btnCheckRegNumIsUsable.setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                    } else {
                        binding.btnCheckRegNumIsUsable.setBackgroundResource(
                            R.drawable.bg_btn_grey_5
                        )

                    }

                    binding.tvCurrRegNum.text = if (it.isEmpty()) {
                        "하이픈(-) 없이 입력해주세요"
                    } else {
                        "(${inputText.length} / 10)"
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.btnCheckRegNumIsUsable.setOnClickListener {
            val inputRegNumber = binding.etCorporateNumber.text.toString()


            if (inputRegNumber.isEmpty()) {
                CommonFunc.showToast(requireContext(), "사업자 등록번호를 입력해주세요")

                return@setOnClickListener
            }


            if (inputRegNumber.length != 10) {
                CommonFunc.showToast(requireContext(), "사업자 번호 10자리를 입력해주세요")
                return@setOnClickListener
            }

            val regNumber = "${inputRegNumber.substring(0, 3)}-${
                inputRegNumber.substring(
                    3,
                    5
                )
            }-${inputRegNumber.substring(5)}"


            viewLifecycleOwner.lifecycleScope.launch {
                val result = viewModel.getStoreInfoByRegNum(regNumber)

                if (result.isSuccessful) {
                    val body = result.body()

                    val response = body?.get(0)

                    response?.market?.let {
                        binding.tvCheckResult.visibility = View.VISIBLE
                        binding.tvCheckResult.text = "해당 사업자 등록번호는 사용하실 수 없습니다"
                        (parentFragment as RestaurantRegisterFragment).setRegNumIsOk(false)

                    } ?: run {

                        withContext(Dispatchers.Main) {
                            binding.tvCheckResult.visibility = View.VISIBLE
                            binding.tvCheckResult.text = "해당 사업자 등록번호를 사용하실 수 있습니다"
                            (parentFragment as RestaurantRegisterFragment).setRegNumIsOk(true)
                            (parentFragment as RestaurantRegisterFragment).getCurrentRestaurant().reg_num =
                                regNumber
                        }

                    }


                } else {

                    withContext(Dispatchers.Main) {
                        binding.tvCheckResult.visibility = View.VISIBLE
                        binding.tvCheckResult.text = "해당 사업자 등록번호를 사용하실 수 있습니다"
                        (parentFragment as RestaurantRegisterFragment).setRegNumIsOk(true)
                        (parentFragment as RestaurantRegisterFragment).getCurrentRestaurant().reg_num =
                            regNumber

                    }

                }
            }


        }
    }

    fun getEtRestaurantName(): String {

        return binding.etRestaurantName.text.toString()


    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


}