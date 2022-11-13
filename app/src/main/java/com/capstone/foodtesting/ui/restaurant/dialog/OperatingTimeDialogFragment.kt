package com.capstone.foodtesting.ui.restaurant.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.capstone.foodtesting.databinding.DialogFragmentDateTimeBinding
import com.capstone.foodtesting.util.Constants.DATE_TIME_KEY
import com.capstone.foodtesting.util.Constants.SELECT_DATE_TIME
import com.capstone.foodtesting.util.DeviceUtil

class OperatingTimeDialogFragment : DialogFragment() {

    private val args by navArgs<OperatingTimeDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogFragmentDateTimeBinding.inflate(layoutInflater)
        binding.datePicker.apply {
            init(
                args.datetime.year,
                args.datetime.month,
                args.datetime.day
            ) { _, year, month, day ->
                args.datetime.apply {
                    this.year = year
                    this.month = month
                    this.day = day
                }
            }
        }

        binding.timePicker.apply {
            if(DeviceUtil.isAndroid6Later()) {
                minute = args.datetime.minute
                hour = args.datetime.hour
            }
            setOnTimeChangedListener { _, hour, minute ->
                args.datetime.apply {
                    this.hour = hour
                    this.minute = minute
                }
            }
        }

        binding.btnSelect.setOnClickListener {

            requireActivity().supportFragmentManager.setFragmentResult(
                SELECT_DATE_TIME,
                Bundle().apply {

                    putParcelable(DATE_TIME_KEY, args.datetime)

                })
            this@OperatingTimeDialogFragment.dismiss()
        }
        binding.btnClose.setOnClickListener {

            this@OperatingTimeDialogFragment.dismiss()
        }
        return AlertDialog.Builder(requireContext()).setView(binding.root).create()

    }

}