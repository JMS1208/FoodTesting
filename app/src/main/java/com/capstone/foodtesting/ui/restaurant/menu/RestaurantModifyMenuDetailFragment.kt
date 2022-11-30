package com.capstone.foodtesting.ui.restaurant.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.capstone.foodtesting.databinding.FragmentRestaurantModifyMenuDetailBinding
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.ui.restaurant.dialog.TestingPeriodDialogFragment
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.CommonFunc.imageRemoveIPPrefix
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.google.android.gms.common.server.converter.StringToIntConverter
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RestaurantModifyMenuDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantModifyMenuDetailBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<RestaurantModifyMenuDetailFragmentArgs>()

    private val viewModel by viewModels<RestaurantModifyMenuDetailViewModel>()

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E)")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantModifyMenuDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .load(args.menu.photoUrl)
            .into(binding.ivMenuImage)

        args.menu.apply {
            photoUrl = imageRemoveIPPrefix(photoUrl)

            hope_price?.let {
                binding.etHopePrice.setText(it.toString())
            }

            discount_price?.let {
                binding.etSalePrice.setText(it.toString())
            }

            explains?.let {
                binding.etMenuExplains.setText(it)
            }

            menuName?.let {
                binding.etMenuName.setText(it)
            }

            ingredients?.let {
                binding.etIngredients.setText(it)
            }

            if (start_date != null && end_date != null) {
                val text = "${simpleDateFormat.format(Date(start_date!!))} ~ ${
                    simpleDateFormat.format(
                        Date(end_date!!)
                    )
                }"

                binding.tvTestingPeriod.text = text
                binding.tvTestingPeriod.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.point_red
                    )
                )
            }

        }



        binding.etIngredients.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.menu.ingredients = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit


        })

        binding.etHopePrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            @SuppressLint("LogNotTimber")
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    args.menu.hope_price = text.toString().toInt()
                } catch (E: Exception) {
                    Log.e(
                        "TAG",
                        "RestaurantModifyMenuDetailFragment() String to Int Convert Exception"
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit


        })

        binding.etMenuName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                args.menu.menuName = text.toString()

            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })

        binding.etMenuExplains.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.menu.explains = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etSalePrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            @SuppressLint("LogNotTimber")
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    args.menu.discount_price = text.toString().toInt()
                } catch (E: Exception) {
                    Log.e(
                        "TAG",
                        "RestaurantModifyMenuDetailFragment() String to Int Convert Exception"
                    )
                }

            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })


        binding.tvTestingPeriod.setOnClickListener {
            val dialog = TestingPeriodDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)

        }


        setFragmentResultListener("TestingPeriod") { _, bundle ->

            val startDate = bundle.getLong("StartDate")
            val endDate = bundle.getLong("EndDate")

            args.menu.start_date = startDate
            args.menu.end_date = endDate


            val text = "${simpleDateFormat.format(Date(startDate))} ~ ${
                simpleDateFormat.format(
                    Date(endDate)
                )
            }"

            binding.tvTestingPeriod.text = text
            binding.tvTestingPeriod.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.point_red
                )
            )

        }

        binding.ivMenuImage.setOnClickListener {
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

                                    args.menu.photoUrl = imageHash

                                    "해당 이미지를 사용할 수 있습니다"

                                } ?: "사진 등록에 실패하였습니다"

                            } else {

                                "사진 등록에 실패하였습니다"
                            }
                        }

                        if (dialog.isVisible) {
                            dialog.dismiss()
                        }

                        showToast(requireContext(), jobResult.await())


                    }


                }
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnModify.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                args.menu.apply {

                    if (this.photoUrl == null) {
                        showToast(requireContext(), "메뉴 사진을 등록해주세요")
                        return@launch
                    }
                    if (this.photoUrl!!.contains("https://foodtesting-img.s3.ap-northeast-2.amazonaws.com/img/")) {
                        this.photoUrl = imageRemoveIPPrefix(this.photoUrl)
                    }

                    if (this.end_date == null || this.start_date == null) {
                        showToast(requireContext(), "테스트 시작 및 종료일을 설정해주세요")
                        return@launch
                    }
                    if (this.hope_price == null) {
                        showToast(requireContext(), "가격을 등록해주세요")
                        return@launch
                    }

                    if (this.discount_price == null) {
                        this.discount_price = hope_price
                    }

                    if (this.explains == null) {
                        showToast(requireContext(), "등록하실 메뉴에 대해서 설명해주세요")
                        return@launch
                    }

                    if (this.ingredients == null) {
                        showToast(requireContext(), "원산지 표기를 입력해주세요")
                        return@launch
                    }

                    if (this.menuName == null) {
                        showToast(requireContext(), "등록하실 메뉴 이름을 입력해주세요")
                        return@launch
                    }
                    update_date = System.currentTimeMillis()
                }


                val result = viewLifecycleOwner.lifecycleScope.async {
                    val response = viewModel.modifyMenu(args.menu)

                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        message == "Success to modify menu"
                    } else {
                        false
                    }
                }.await()

                if (result) {
                    showToast(requireContext(), "메뉴가 수정되었습니다")
                    findNavController().popBackStack()
                } else {
                    showToast(requireContext(), "메뉴 수정에 실패하였습니다")
                }


            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}