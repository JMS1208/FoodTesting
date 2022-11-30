package com.capstone.foodtesting.ui.restaurant.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.databinding.FragmentRestaurantAddMenuBinding
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.ui.restaurant.dialog.TestingPeriodDialogFragment
import com.capstone.foodtesting.util.CommonFunc

import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RestaurantAddMenuFragment : Fragment() {

    private var _binding: FragmentRestaurantAddMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RestaurantAddMenuViewModel>()

    private var startDate: Date = Date(System.currentTimeMillis())

    private var endDate: Date = Date(System.currentTimeMillis())

//    private lateinit var marketInfo: Restaurant

    private val args by navArgs<RestaurantAddMenuFragmentArgs>()

    private lateinit var newMenu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantAddMenuBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newMenu = Menu(
            post_uuid = UUID.randomUUID().toString(),
            regNumber = args.regNum,
            customer_uuid = args.customerUuid
        )


        binding.etMenuName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newMenu.menuName = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etIngredients.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newMenu.ingredients = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etHopePrice.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    newMenu.hope_price = text.toString().toInt()
                } catch(E: Exception) {
                    E.printStackTrace()
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etMenuExplains.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newMenu.explains = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) =Unit

        })

        binding.etSalePrice.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    newMenu.discount_price = text.toString().toInt()
                } catch(E: Exception) {
                    E.printStackTrace()
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })



        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
//
        binding.ivAddMenuImage.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val dialog = DialogLoading()
                        dialog.show(childFragmentManager, dialog.tag)

                        val result =
                            viewModel.uploadRestaurantImage(it)


                        val jobResult = viewLifecycleOwner.lifecycleScope.async {
                            if (result.isSuccessful) {
                                result.body()?.imageHash?.let { imageHash ->

                                    Glide.with(requireContext())
                                        .load(it)
                                        .into(binding.ivMenuImage)

                                    newMenu.photoUrl = imageHash

                                    "해당 이미지를 사용할 수 있습니다"

                                } ?: "사진 등록에 실패하였습니다"

                            } else {

                                "사진 등록에 실패하였습니다"
                            }
                        }

                        if (dialog.isVisible) {
                            dialog.dismiss()
                        }

                        CommonFunc.showToast(requireContext(), jobResult.await())


                    }


                }
        }


        binding.tvTestingPeriod.setOnClickListener {
            val dialog = TestingPeriodDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }


        setFragmentResultListener("TestingPeriod") { _, bundle ->

            val startDate = bundle.getLong("StartDate")
            val endDate = bundle.getLong("EndDate")

            this@RestaurantAddMenuFragment.startDate = Date(startDate)
            this@RestaurantAddMenuFragment.endDate = Date(endDate)

            newMenu.start_date = startDate
            newMenu.end_date = endDate

            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E)")
            val text = "${simpleDateFormat.format(this@RestaurantAddMenuFragment.startDate)} ~ ${simpleDateFormat.format(this@RestaurantAddMenuFragment.endDate)}"

            binding.tvTestingPeriod.text = text
            binding.tvTestingPeriod.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.point_red
                )
            )

        }

        binding.btnAdd.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {

                newMenu.apply {
                    if(this.photoUrl == null) {
                        CommonFunc.showToast(requireContext(), "메뉴 사진을 등록해주세요")
                        return@launch
                    }
                    if(this.end_date == null || this.start_date == null) {
                        CommonFunc.showToast(requireContext(), "테스트 시작 및 종료일을 설정해주세요")
                        return@launch
                    }
                    if(this.hope_price == null) {
                        CommonFunc.showToast(requireContext(), "가격을 등록해주세요")
                        return@launch
                    }

                    if(this.discount_price == null) {
                        this.discount_price = hope_price
                    }

                    if(this.explains == null) {
                        CommonFunc.showToast(requireContext(), "등록하실 메뉴에 대해서 설명해주세요")
                        return@launch
                    }

                    if(this.ingredients == null) {
                        CommonFunc.showToast(requireContext(), "원산지 표기를 입력해주세요")
                        return@launch
                    }

                    if(this.menuName == null) {
                        CommonFunc.showToast(requireContext(), "등록하실 메뉴 이름을 입력해주세요")
                        return@launch
                    }

                }


                val result = viewModel.postNewMenu(newMenu)

                if (result.isSuccessful) {
                    CommonFunc.showToast(requireContext(), "정상적으로 등록되었습니다")
                    findNavController().popBackStack()
                }
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}