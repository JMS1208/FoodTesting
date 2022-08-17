package com.capstone.foodtesting.ui.register

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import dagger.hilt.android.AndroidEntryPoint
import com.capstone.foodtesting.databinding.FragmentRegisterBinding
import org.intellij.lang.annotations.JdkConstants
import java.time.Month
import java.time.MonthDay
import java.time.Year

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

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var calendar=java.util.Calendar.getInstance()
        val year=calendar.get(java.util.Calendar.YEAR)
        val month=calendar.get(java.util.Calendar.MONTH)
        val day=calendar.get(java.util.Calendar.DAY_OF_MONTH)
        var uYear=Calendar.YEAR
        var uMonth=Calendar.MONTH
        var uDay=Calendar.DAY_OF_MONTH

        binding.btnClose.setOnClickListener{
            val action = RegisterFragmentDirections.actionRegisterFragmentToFragmentLogin()
            findNavController().navigate(action)
        }
        binding.tvBornDateBtn.setOnClickListener {
            val datePickerDialog=DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                binding.tvBornDate.setText(""+myear+". "+mmonth+". "+mdayOfMonth)
                uYear=myear
                uMonth=mmonth
                uDay=mdayOfMonth
            },year,month,day)
            datePickerDialog.datePicker.spinnersShown=true
            datePickerDialog.show()
        }
        binding.btnNext.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToRegisterFinishedFragment()
            if (binding.etName.text.isNullOrBlank()){
                Toast.makeText(context,"이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else if (binding.etEmail.text.isNullOrBlank()){
                Toast.makeText(context,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else if (binding.etPassword.text.isNullOrBlank()){
                Toast.makeText(context,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else if (!(binding.cbFemale.isChecked || binding.cbMale.isChecked)){
                Toast.makeText(context,"성별을 체크해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                // TODO("BE로 회원가입 정보 등록 요청")
                // 앱 내에 데이터 저장
                viewModel.name= MutableLiveData(binding.etName.text.toString())
                viewModel.email=MutableLiveData(binding.etEmail.text.toString())
                viewModel.pw= MutableLiveData(binding.etPassword.text.toString())
                viewModel.gender=if (binding.cbFemale.isChecked) MutableLiveData("여") else MutableLiveData("남")
                viewModel.birthYear=MutableLiveData(uYear.toString())
                viewModel.birthDay=MutableLiveData(uMonth.toString()+uDay.toString())
                viewModel.saveUserData()
                findNavController().navigate(action)
            }


        }


        //성별 체크박스


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}