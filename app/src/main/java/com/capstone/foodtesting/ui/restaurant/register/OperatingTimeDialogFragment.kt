package com.capstone.foodtesting.ui.restaurant.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.DialogDatePickerOperatingTimeBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.dialogFragmentResize
import java.text.SimpleDateFormat
import java.util.*

class OperatingTimeDialogFragment : DialogFragment() {

    private var _binding: DialogDatePickerOperatingTimeBinding? = null
    private val binding get() = _binding!!

    private var openTime: Long? = null
    private var closeTime: Long? = null

    private var currentTime: MutableLiveData<OperatingTime> = MutableLiveData(OperatingTime.OPEN)
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("aa HH:mm")
    enum class OperatingTime {
        OPEN,
        CLOSE
    }

    private val systemCalendar = java.util.Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDatePickerOperatingTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.TransparentDialogDim)

    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelect.setOnClickListener {

//            CommonFunc.showToast(requireContext(), "$openTime, ${simpleDateFormat.format(Date(openTime?:0))}")
            openTime?: CommonFunc.showToast(requireContext(), "오픈 시간을 설정해주세요")



            closeTime?: CommonFunc.showToast(requireContext(), "클로즈 시간을 설정해주세요")


            if (openTime != null && closeTime != null) {
                parentFragment?.setFragmentResult("OperatingTime", Bundle().apply {
                    putLong("OpenTime", openTime!!)
                    putLong("CloseTime", closeTime!!)
                })

                dismiss()
            }
        }

        binding.tvOpen.setOnClickListener {
            currentTime.postValue(OperatingTime.OPEN)

        }

        binding.tvClose.setOnClickListener {
            currentTime.postValue(OperatingTime.CLOSE)
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->

            val calendar = GregorianCalendar(2022,0,0,hourOfDay, minute)

            when (currentTime.value) {
                OperatingTime.OPEN -> {
                    val newTime: Long = calendar.timeInMillis
                    openTime = newTime
                    binding.tvOpenTime.text = simpleDateFormat.format(Date(openTime?:0))
                }
                OperatingTime.CLOSE -> {
                    val newTime: Long = calendar.timeInMillis
                    closeTime = newTime
                    binding.tvCloseTime.text = simpleDateFormat.format(Date(closeTime?:0))
                }
                else -> return@setOnTimeChangedListener
            }

        }
        currentTime.observe(viewLifecycleOwner) { operatingTime ->
            when (operatingTime) {
                OperatingTime.OPEN -> {
                    ContextCompat.getColor(requireContext(), R.color.grey).let {
                        binding.tvClose.setTextColor(it)
                        binding.tvCloseTime.setTextColor(it)
                    }
                    ContextCompat.getColor(requireContext(), R.color.point_red).let {
                        binding.tvOpen.setTextColor(it)
                        binding.tvOpenTime.setTextColor(it)
                    }
                }
                OperatingTime.CLOSE -> {
                    ContextCompat.getColor(requireContext(), R.color.grey).let {
                        binding.tvOpen.setTextColor(it)
                        binding.tvOpenTime.setTextColor(it)
                    }
                    ContextCompat.getColor(requireContext(), R.color.point_red).let {
                        binding.tvClose.setTextColor(it)
                        binding.tvCloseTime.setTextColor(it)
                    }
                }
                else -> return@observe
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}