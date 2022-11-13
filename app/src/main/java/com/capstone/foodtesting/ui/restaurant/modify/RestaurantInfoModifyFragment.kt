package com.capstone.foodtesting.ui.restaurant.modify

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentRestaurantInfoModifyBinding
import com.capstone.foodtesting.ui.common.dialog.AddressSettingsDialogFragment
import com.capstone.foodtesting.ui.restaurant.register.OperatingTimeDialogFragment
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.SpinnerGravity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RestaurantInfoModifyFragment : Fragment() {

    private var _binding: FragmentRestaurantInfoModifyBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RestaurantInfoModifyFragmentArgs>()

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

        binding.spinnerCategory.apply {
            setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                Toast.makeText(requireContext(), newItem, Toast.LENGTH_SHORT).show()
            }
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
                    IconSpinnerItem(text = "한식", iconRes = R.drawable.ic_category_hansik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "중식", iconRes = R.drawable.ic_category_jungsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "일식", iconRes = R.drawable.ic_category_ilsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "양식", iconRes = R.drawable.ic_category_yangsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "패스트푸드", iconRes = R.drawable.ic_category_fastfood_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "분식", iconRes = R.drawable.ic_category_bunsik_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "디저트", iconRes = R.drawable.ic_category_dessert_30, textTypeface = typeface, gravity = Gravity.CENTER),
                    IconSpinnerItem(text = "기타", iconRes = R.drawable.ic_category_others_30, textTypeface = typeface, gravity = Gravity.CENTER)
                )
            )
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(context, 2)
//            selectItemByIndex(0) // select a default item.
            //여기서 기존 카테고리 설정
            lifecycleOwner = viewLifecycleOwner
        }

        setFragmentResultListener("OperatingTime"){ _, bundle ->

            val openTime: Long
            val closeTime: Long

            bundle.getLong("OpenTime").let {

                openTime = it
            }
            bundle.getLong("CloseTime").let {

                closeTime = it
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

            binding.tvAddress.text = addressText
            binding.tvAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))
        }

        initRestaurantInfo()
    }


    @SuppressLint("SimpleDateFormat")
    private fun initRestaurantInfo() {
        args.restaurant.let {
            when(it.category) {
                "한식"-> {
                    binding.spinnerCategory.selectItemByIndex(0)
                }
                "중식"-> {
                    binding.spinnerCategory.selectItemByIndex(1)
                }
                "일식"-> {
                    binding.spinnerCategory.selectItemByIndex(2)
                }
                "양식"-> {
                    binding.spinnerCategory.selectItemByIndex(3)
                }
                "패스트푸드"-> {
                    binding.spinnerCategory.selectItemByIndex(4)
                }
                "분식"-> {
                    binding.spinnerCategory.selectItemByIndex(5)
                }
                "디저트"-> {
                    binding.spinnerCategory.selectItemByIndex(6)
                }
                "기타"-> {
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