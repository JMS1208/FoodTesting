package com.capstone.foodtesting.ui.member.signup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentMemberSignUpBinding
import com.capstone.foodtesting.util.CommonFunc.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MemberSignUpFragment : Fragment() {

    private var _binding: FragmentMemberSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MemberSignUpViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        var uYear = Calendar.YEAR
        var uMonth = Calendar.MONTH
        var uDay = Calendar.DAY_OF_MONTH



        binding.tvBornDate.setOnClickListener {
            val datePickerDialog=
                DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                binding.tvBornDate.text = "${myear}??? ${mmonth+1}??? ${mdayOfMonth}???"
                uYear=myear
                uMonth=mmonth+1
                uDay=mdayOfMonth
            },year,month,day)
            datePickerDialog.datePicker.spinnersShown=true
            datePickerDialog.show()
        }


        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignUp.setOnClickListener {
            //??????
            val noticeText = when (checkEmptyInfo()) {
                SignUpNotice.NAME_EMPTY -> {
                    "????????? ??????????????????"
                }
                SignUpNotice.GENDER_EMPTY -> {
                    "????????? ??????????????????"
                }
                SignUpNotice.NICKNAME_EMPTY -> {
                    "???????????? ??????????????????"
                }
                SignUpNotice.EMAIL_EMPTY -> {
                    "???????????? ??????????????????"
                }
                SignUpNotice.EMAIL_PATTERN_ERROR -> {
                    "????????? ????????? ??????????????????"
                }
                SignUpNotice.BORN_DATE_EMPTY -> {
                    "??????????????? ??????????????????"
                }
                SignUpNotice.PASSWORD_EMPTY -> {
                    "??????????????? ??????????????????"
                }
                SignUpNotice.EVERYTHING_IS_OKAY -> {
                    runRegister()

                    ""
                }
            }
            binding.tvNotice.text = noticeText

        }

        binding.btnMale.setOnClickListener {
            viewModel.updateGender(Member.MALE)
        }

        binding.btnFemale.setOnClickListener {
            viewModel.updateGender(Member.FEMALE)
        }

//        viewModel.mutableSignUpSuccess.observe(viewLifecycleOwner) { member->
//            member?.let {
//                viewModel.insertMember(it)
//
//                setFragmentResult("SignUp", Bundle().apply {
//                    putBoolean("SignUp", true)
//                })
//
//                findNavController().popBackStack()
//
//            }
//
//        }

        viewModel.mutableGender.observe(viewLifecycleOwner) { gender ->
            gender?.let {
                when (gender) {
                    Member.MALE -> {
                        binding.btnMale.apply {
                            setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }

                        binding.btnFemale.apply {
                            setBackgroundResource(R.drawable.bg_rounded_stroke_black_5)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        }
                    }
                    Member.FEMALE -> {
                        binding.btnFemale.apply {
                            setBackgroundResource(R.drawable.bg_btn_gradient2_5)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                        }

                        binding.btnMale.apply {
                            setBackgroundResource(R.drawable.bg_rounded_stroke_black_5)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        }
                    }
                    else -> return@observe
                }
            }
        }
    }
    private fun runRegister() =
        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val member = createMember()

                val response = viewModel.registerUserInfo(member)

                if (response.isSuccessful) {
                    val msg = response.body()?.message

                    when (msg) {
                        "Success to Add" -> {
                            finishUserRegister(member)
                        }
                        "Failed to Add" -> {
                            showToast(requireContext(), "??????????????? ??????????????????")
                        }
                        "email is already exists" -> {
                            showToast(requireContext(), "?????? ???????????? ????????? ?????????")
                        }
                        else -> {
                            showToast(requireContext(), "??? ??? ?????? ????????? ??????????????????")

                        }
                    }
                }
            } catch (E: Exception) {
                showToast(requireContext(), "${E.message}")
            }

        }

    private fun finishUserRegister(member: Member) {
        viewModel.insertMember(member)

//        setFragmentResult("SignUp", Bundle().apply {
//            putBoolean("SignUp", true)
//        })

        showToast(requireContext(), "??????????????? ?????????????????????")

        findNavController().popBackStack()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createMember(): Member {
        val dateFormat = SimpleDateFormat("yyyy??? MM??? dd???")

        val name = binding.etName.text.toString()
        val nickName = binding.etNickname.text.toString()
        val gender = viewModel.mutableGender.value ?: 0
        val email = binding.etEmail.text.toString()
        val birthDate = binding.tvBornDate.text.toString()
        val password = binding.etPassword.text.toString()

        return Member(
//            age = 0,
            email = email,
            name = name,
            nickName = nickName,
            gender = gender,
            birthDate = dateFormat.parse(birthDate).time,
            password = password
        )
    }

    private fun checkEmptyInfo(): SignUpNotice {

        if (binding.etName.text.isEmpty()) {
            return SignUpNotice.NAME_EMPTY
        }

        if (viewModel.mutableGender.value == 0 || viewModel.mutableGender.value == null) {
            return SignUpNotice.GENDER_EMPTY
        }

        if (binding.etNickname.text.isEmpty()) {
            return SignUpNotice.NICKNAME_EMPTY
        }

        if (binding.etEmail.text.isEmpty()) {
            return SignUpNotice.EMAIL_EMPTY
        } else {
            val value: String = binding.etEmail.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!value.matches(emailPattern.toRegex())) {
                return SignUpNotice.EMAIL_PATTERN_ERROR
            }
        }

        if (binding.tvBornDate.text.isEmpty()) {
            return SignUpNotice.BORN_DATE_EMPTY
        }

        if (binding.etPassword.text.isEmpty()) {
            return SignUpNotice.PASSWORD_EMPTY
        }

        return SignUpNotice.EVERYTHING_IS_OKAY
    }

    enum class SignUpNotice {
        NAME_EMPTY,
        GENDER_EMPTY,
        NICKNAME_EMPTY,
        EMAIL_EMPTY,
        EMAIL_PATTERN_ERROR,
        BORN_DATE_EMPTY,
        PASSWORD_EMPTY,
        EVERYTHING_IS_OKAY
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}