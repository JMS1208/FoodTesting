package com.capstone.foodtesting.ui.restaurant.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.TimeUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.DialogDatePickerTestingPeriodBinding
import com.capstone.foodtesting.util.dialogFragmentResize
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TestingPeriodDialogFragment : DialogFragment() {

    private var _binding: DialogDatePickerTestingPeriodBinding? = null
    private val binding get() = _binding!!

    private var startDate: Long? = null
    private var endDate: Long? = null

    private var currentDate: MutableLiveData<TestingPeriod> = MutableLiveData(
        TestingPeriod.START
    )

    enum class TestingPeriod {
        START,
        END
    }

    private val calendar = java.util.Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDatePickerTestingPeriodBinding.inflate(inflater, container, false)
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
            startDate ?: Toast.makeText(requireContext(), "시작일을 설정해주세요", Toast.LENGTH_SHORT).show()


            endDate ?: Toast.makeText(requireContext(), "종료일을 설정해주세요", Toast.LENGTH_SHORT).show()


            if (startDate != null && endDate != null) {

                parentFragment?.setFragmentResult("TestingPeriod", Bundle().apply {
                    putLong("StartDate", startDate!!)
                    putLong("EndDate", endDate!!)
                })

                dismiss()
            }
        }


        binding.datePicker.apply {
            minDate = System.currentTimeMillis()

            init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            ) { _, year, month, day ->


                val gregorianCalendar = GregorianCalendar(year, month, day)

                when (currentDate.value) {
                    TestingPeriod.START -> {
                        startDate = gregorianCalendar.timeInMillis
                        binding.tvStartDate.text = "$year.${month + 1}.$day"
                    }
                    TestingPeriod.END -> {
                        endDate = gregorianCalendar.timeInMillis
                        binding.tvEndDate.text = "$year.${month + 1}.$day"
                    }
                    else -> Unit
                }


            }
        }


        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.tvStart.setOnClickListener {
            currentDate.postValue(TestingPeriod.START)
        }

        binding.tvEnd.setOnClickListener {
            currentDate.postValue(TestingPeriod.END)
        }



        currentDate.observe(viewLifecycleOwner) { testingPeriod ->
            when (testingPeriod) {
                TestingPeriod.START -> {
                    ContextCompat.getColor(requireContext(), R.color.grey).let {
                        binding.tvEnd.setTextColor(it)
                        binding.tvEndDate.setTextColor(it)
                    }
                    ContextCompat.getColor(requireContext(), R.color.point_red).let {
                        binding.tvStart.setTextColor(it)
                        binding.tvStartDate.setTextColor(it)
                    }
                }
                TestingPeriod.END -> {
                    ContextCompat.getColor(requireContext(), R.color.grey).let {
                        binding.tvStart.setTextColor(it)
                        binding.tvStartDate.setTextColor(it)
                    }
                    ContextCompat.getColor(requireContext(), R.color.point_red).let {
                        binding.tvEnd.setTextColor(it)
                        binding.tvEndDate.setTextColor(it)
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