package com.capstone.foodtesting.ui.infoRev

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentInforevBinding
import com.capstone.foodtesting.ui.info.InfoViewModel
import com.capstone.foodtesting.ui.register.RegisterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class InfoRevFragment: Fragment(){
    private var _binding: FragmentInforevBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<InfoRevViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInforevBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var calendar=java.util.Calendar.getInstance()
        val year=calendar.get(java.util.Calendar.YEAR)
        val month=calendar.get(java.util.Calendar.MONTH)
        val day=calendar.get(java.util.Calendar.DAY_OF_MONTH)
        var uYear= Calendar.YEAR
        var uMonth= Calendar.MONTH
        var uDay= Calendar.DAY_OF_MONTH


        binding.btnClose.setOnClickListener{
            val action = RegisterFragmentDirections.actionRegisterFragmentToFragmentLogin()
            findNavController().navigate(action)
        }
        binding.tvBornDateBtn.setOnClickListener {
            val datePickerDialog= DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                binding.tvBornDate.setText(""+myear+". "+(mmonth+1)+". "+mdayOfMonth)
                uYear=myear
                uMonth=mmonth+1
                uDay=mdayOfMonth
            },year,month,day)
            datePickerDialog.datePicker.spinnersShown=true
            datePickerDialog.show()
        }

        binding.btnNext.setOnClickListener {
            val nickName=binding.etNickname.text.toString()
            val gender=if (binding.cbMale.isChecked) Member.MALE else Member.FEMALE
            val birthDate= Date(uYear,uMonth,uDay)
            viewModel.updateMemeber(nickName,gender,birthDate)
            // TODO(수정된 멤버 정보 BE로 보내기)
            val action=InfoRevFragmentDirections.actionInfoRevFragment2ToFragmentInfo()
            findNavController().navigate(action)
        }

        binding.btnClose.setOnClickListener {
            val action=InfoRevFragmentDirections.actionInfoRevFragment2ToFragmentInfo()
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMember.collectLatest { memberInfo ->
                    memberInfo?.let {
                        binding.etNickname.setText("${it.nickName}")
                        if (it.gender==Member.MALE){
                            binding.cbMale.isChecked=true
                        }
                        else if (it.gender==Member.FEMALE){
                            binding.cbFemale.isChecked=true
                        }
                        binding.tvBornDate.setText("${it.birthDate.year}.${it.birthDate.month}.${it.birthDate.day}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}