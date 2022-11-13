package com.capstone.foodtesting.ui.restaurant.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentRestaurantRegisterOperatingTimeBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class OperatingTimeFragment : Fragment() {

    private var _binding: FragmentRestaurantRegisterOperatingTimeBinding? = null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantRegisterOperatingTimeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvOperatingTime.setOnClickListener {
            val dialog = OperatingTimeDialogFragment()

            dialog.show(childFragmentManager, dialog.tag)
        }

        setFragmentResultListener("OperatingTime"){ _, bundle ->
            val restaurant = (parentFragment as RestaurantRegisterFragment).getCurrentRestaurant()

            val openTime: Long
            val closeTime: Long

            bundle.getLong("OpenTime").let {
                restaurant.openTime = it
                openTime = it
            }
            bundle.getLong("CloseTime").let {
                restaurant.closeTime = it
                closeTime = it
            }
            val simpleDateFormat = SimpleDateFormat("aa HH:mm")

            val openTimeText = simpleDateFormat.format(Date(openTime))
            val closeTimeText = simpleDateFormat.format(Date(closeTime))



            binding.tvOperatingTime.text = "$openTimeText ~ $closeTimeText"
            binding.tvOperatingTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.point_red))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}