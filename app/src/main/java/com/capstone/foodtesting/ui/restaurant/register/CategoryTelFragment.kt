package com.capstone.foodtesting.ui.restaurant.register

import android.graphics.Typeface
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterCategoryTelAddrBinding
import com.capstone.foodtesting.ui.common.dialog.AddressSettingsDialogFragment
import com.capstone.foodtesting.util.CommonFunc
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.SpinnerGravity


class CategoryTelFragment : Fragment() {

    private var _binding: FragmentRestaurantRegisterCategoryTelAddrBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentRestaurantRegisterCategoryTelAddrBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.etTelephoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.spinnerCategory.apply {

            setSpinnerAdapter(IconSpinnerAdapter(this))

            arrowAnimate = true
            arrowGravity = SpinnerGravity.END
            setOnSpinnerOutsideTouchListener { _, _ ->
                this.dismiss()
            }
            var typeface: Typeface? = null
            activity?.assets?.let {
                typeface = Typeface.createFromAsset(it, "fontasset/noto_sans_kr_regular.otf")
            }
            setItems(
                arrayListOf(
                    IconSpinnerItem(
                        text = "한식",
                        iconRes = R.drawable.ic_category_hansik_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "중식",
                        iconRes = R.drawable.ic_category_jungsik_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "일식",
                        iconRes = R.drawable.ic_category_ilsik_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "양식",
                        iconRes = R.drawable.ic_category_yangsik_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "패스트푸드",
                        iconRes = R.drawable.ic_category_fastfood_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "분식",
                        iconRes = R.drawable.ic_category_bunsik_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "디저트",
                        iconRes = R.drawable.ic_category_dessert_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    ),
                    IconSpinnerItem(
                        text = "기타",
                        iconRes = R.drawable.ic_category_others_30,
                        textTypeface = typeface,
                        gravity = Gravity.CENTER
                    )
                )
            )
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(context, 2)
//            selectItemByIndex(0) // select a default item.
            lifecycleOwner = viewLifecycleOwner

            setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->
                CommonFunc.showToast(requireContext(), "${newItem.text} 선택하셨습니다")

                val category = newItem.text.toString()

                val restaurant = getParentRestaurant()

                restaurant.category = category
            }
        }

        binding.tvAddress.setOnClickListener {
            val dialog = AddressSettingsDialogFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }

        setFragmentResultListener("AddressSetting") { _, bundle ->
            //받은 정보
            val addressText = bundle.getString("Address")
            val roadAddressText = bundle.getString("RoadAddress")
            val longitude = bundle.getDouble("Longitude")
            val latitude = bundle.getDouble("Latitude")

            binding.tvAddress.text = "$addressText"
            binding.tvAddress.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.point_red
                )
            )

            val restaurant = getParentRestaurant()

            restaurant.apply {
                this.address = addressText
                this.roadAddress = roadAddressText
                this.longitude = longitude
                this.latitude = latitude
            }

        }
    }

    private fun getParentRestaurant(): Restaurant {
        return (parentFragment as RestaurantRegisterFragment).getCurrentRestaurant()
    }

    fun getEtTelephoneNumber(): String {
        return binding.etTelephoneNumber.text.toString().replace("-","")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}