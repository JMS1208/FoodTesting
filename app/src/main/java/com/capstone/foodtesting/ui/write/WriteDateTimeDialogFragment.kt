package com.capstone.foodtesting.ui.write

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresPermission
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.foodtesting.data.model.date.DateTime
import com.capstone.foodtesting.databinding.DialogFragmentDateTimeBinding
import com.capstone.foodtesting.util.Constants.DATE_TIME_KEY
import com.capstone.foodtesting.util.Constants.SELECT_DATE_TIME
import com.capstone.foodtesting.util.DeviceUtil

import java.util.*

class WriteDateTimeDialogFragment : DialogFragment() {

    private val args by navArgs<WriteDateTimeDialogFragmentArgs>()

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
            this@WriteDateTimeDialogFragment.dismiss()
        }
        binding.btnClose.setOnClickListener {

            this@WriteDateTimeDialogFragment.dismiss()
        }
        return AlertDialog.Builder(requireContext()).setView(binding.root).create()

    }

}