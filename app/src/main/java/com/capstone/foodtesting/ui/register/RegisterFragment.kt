package com.capstone.foodtesting.ui.register

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import dagger.hilt.android.AndroidEntryPoint
import com.capstone.foodtesting.databinding.FragmentRegisterBinding

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        var calendar=java.util.Calendar.getInstance()
        val year=calendar.get(java.util.Calendar.YEAR)
        val month=calendar.get(java.util.Calendar.MONTH)
        val day=calendar.get(java.util.Calendar.DAY_OF_MONTH)

        binding.tvBornDateBtn.setOnClickListener {
            val datePickerDialog=DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                binding.tvBornDate.setText(""+myear+". "+mmonth+". "+mdayOfMonth)
            },year,month,day)
            datePickerDialog.datePicker.spinnersShown=true
            datePickerDialog.show()
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener{
            val action = RegisterFragmentDirections.actionRegisterFragmentToFragmentLogin()
            findNavController().navigate(action)
        }

        binding.btnNext.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToRegisterFinishedFragment()
            findNavController().navigate(action)
        }

        binding.tvBornDate.setOnClickListener {
            //여기서 DatePicker 누를 수 있게

        }

        //성별 체크박스


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}