package com.capstone.foodtesting.ui.restaurant.modify

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentRestaurantInfoModifyBinding
import com.capstone.foodtesting.ui.common.dialog.AddressSettingsDialogFragment
import com.capstone.foodtesting.ui.loading.DialogLoading
import com.capstone.foodtesting.ui.restaurant.register.OperatingTimeDialogFragment
import com.capstone.foodtesting.util.CommonFunc
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.SpinnerGravity
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RestaurantInfoModifyFragment : Fragment() {

    private var _binding: FragmentRestaurantInfoModifyBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RestaurantInfoModifyFragmentArgs>()

    private val viewModel by viewModels<RestaurantInfoModifyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantInfoModifyBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbarView()

        binding.ivModifyImage.setOnClickListener {
            TedImagePicker.with(requireContext())
                .start { uri ->
                    uri.let {

                        viewLifecycleOwner.lifecycleScope.launch {
                            val dialog = DialogLoading()
                            dialog.show(childFragmentManager, dialog.tag)

                            val result =
                                viewModel.uploadRestaurantImage(it)


                            val jobResult = viewLifecycleOwner.lifecycleScope.async {
                                if (result.isSuccessful) {
                                    result.body()?.imageHash?.let { imageHash->

                                        Glide.with(requireContext())
                                            .load(it)
                                            .into(binding.ivRestaurantImage)

                                        args.restaurant.apply {
                                            this.photoUrl = imageHash
                                        }

                                        "?????? ???????????? ????????? ??? ????????????"

                                    } ?: "?????? ????????? ?????????????????????"

                                } else {

                                    "?????? ????????? ?????????????????????"
                                }
                            }

                            if (dialog.isVisible) {
                                dialog.dismiss()
                            }

                            CommonFunc.showToast(requireContext(), jobResult.await() )


                        }


                    }

                }
        }

        binding.etHoliday.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.restaurant.holiday = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etRestaurantName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.restaurant.name = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.tvOperatingTime.setOnClickListener {
            val dialog = OperatingTimeDialogFragment()

            dialog.show(childFragmentManager, dialog.tag)
        }

        binding.tvAddress.setOnClickListener {
            val dialog = AddressSettingsDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnComplexity0.setOnClickListener {
            viewModel.setComplexity(0)
        }

        binding.btnComplexity1.setOnClickListener {
            viewModel.setComplexity(1)
        }

        binding.btnComplexity2.setOnClickListener {
            viewModel.setComplexity(2)
        }

        args.restaurant.complexity?.let {
            val complexity = when(it) {
                "??????" -> 0
                "??????" -> 1
                "??????" -> 2
                else -> 1
            }

            viewModel.setComplexity(complexity)
        }

        viewModel.complexityLiveData.observe(viewLifecycleOwner) { idx ->

            idx?.let {
                when(it) {
                    0 -> { //???????????????
                        binding.btnComplexity0.setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                        binding.btnComplexity1.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        binding.btnComplexity2.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        args.restaurant.complexity="??????"
                    }
                    1 -> { //???????????????
                        binding.btnComplexity0.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        binding.btnComplexity1.setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                        binding.btnComplexity2.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        args.restaurant.complexity="??????"
                    }
                    2 -> { //???????????????
                        binding.btnComplexity0.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        binding.btnComplexity1.setBackgroundResource(R.drawable.bg_btn_grey_5)
                        binding.btnComplexity2.setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                        args.restaurant.complexity="??????"
                    }
                    else -> Unit
                }
            }

        }


        binding.btnModify.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                //TODO {?????? ???????????? api ????????? ?????? ?????? ????????? ????????? ??????}

                args.restaurant.photoUrl = args.restaurant.photoUrl.toString().replace("https://foodtesting-img.s3.ap-northeast-2.amazonaws.com/img/", "")

                val response = viewModel.modifyRestaurantInfo(args.restaurant)

                if(response.isSuccessful) {
                    val msg = response.body()?.message

                    if (msg == "Success to modify") {
                        CommonFunc.showToast(requireContext(), "??????????????? ?????????????????????")
                        findNavController().popBackStack()
                    } else {
                        CommonFunc.showToast(requireContext(), "???????????? ????????? ????????? ??????????????????")
                    }

                } else {
                    CommonFunc.showToast(requireContext(), "???????????? ????????? ????????? ??????????????????")
                }
            }
        }

        binding.etTelephoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.etTelephoneNumber.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.restaurant.telephoneNumber = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.spinnerCategory.apply {

            setSpinnerAdapter(IconSpinnerAdapter(this))

            arrowAnimate = true
            arrowGravity = SpinnerGravity.END
            setOnSpinnerOutsideTouchListener { _, _ ->
                this.dismiss()
            }
            var typeface: Typeface? = null
            activity?.assets?.let {
                typeface = Typeface.createFromAsset(it,"fontasset/noto_sans_kr_regular.otf")
            }
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_hansik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_jungsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_ilsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_yangsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "???????????????", iconRes = R.drawable.ic_category_fastfood_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_bunsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "?????????", iconRes = R.drawable.ic_category_dessert_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "??????", iconRes = R.drawable.ic_category_others_30, textTypeface = typeface, gravity = Gravity.CENTER)
                )
            )
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(context, 2)
//            selectItemByIndex(0) // select a default item.
            //????????? ?????? ???????????? ??????
            lifecycleOwner = viewLifecycleOwner

            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, _, newItem ->
                args.restaurant.category = newItem.text.toString()
            }
        }

        setFragmentResultListener("OperatingTime"){ _, bundle ->

            val openTime: Long
            val closeTime: Long

            bundle.getLong("OpenTime").let {
                openTime = it
                args.restaurant.openTime = it
            }
            bundle.getLong("CloseTime").let {
                closeTime = it
                args.restaurant.closeTime = it
            }

            val simpleDateFormat = SimpleDateFormat("aa HH:mm")

            val openTimeText = simpleDateFormat.format(Date(openTime))
            val closeTimeText = simpleDateFormat.format(Date(closeTime))

            binding.tvOperatingTime.text = "$openTimeText ~ $closeTimeText"
            binding.tvOperatingTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))


        }

        setFragmentResultListener("AddressSetting") { _, bundle ->
            val addressText = bundle.getString("Address")
            val roadAddressText = bundle.getString("RoadAddress")
            val longitude = bundle.getDouble("Longitude")
            val latitude = bundle.getDouble("Latitude")

            args.restaurant.apply {
                this.address = addressText
                this.roadAddress = roadAddressText
                this.longitude = longitude
                this.latitude = latitude
            }

            binding.tvAddress.text = addressText
            binding.tvAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))
        }

        initRestaurantInfo()
    }

    private fun initToolbarView() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun initRestaurantInfo() {
        args.restaurant.let {
            when(it.category) {
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(0)
                }
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(1)
                }
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(2)
                }
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(3)
                }
                "???????????????"-> {
                    binding.spinnerCategory.selectItemByIndex(4)
                }
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(5)
                }
                "?????????"-> {
                    binding.spinnerCategory.selectItemByIndex(6)
                }
                "??????"-> {
                    binding.spinnerCategory.selectItemByIndex(7)
                }
                else-> Unit
            }

            binding.etRestaurantName.setText(it.name)
            binding.etHoliday.setText(it.holiday)
            binding.etTelephoneNumber.setText(it.telephoneNumber)
            binding.tvAddress.text = it.address

            val simpleDateFormat = SimpleDateFormat("aa HH:mm")

            val openTimeText = simpleDateFormat.format(Date(it.openTime ?: 0))
            val closeTimeText = simpleDateFormat.format(Date(it.closeTime ?: 0))

            binding.tvOperatingTime.text = "$openTimeText ~ $closeTimeText"
            binding.tvOperatingTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))

            Glide.with(requireContext())
                .load(it.photoUrl)
                .into(binding.ivRestaurantImage)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}