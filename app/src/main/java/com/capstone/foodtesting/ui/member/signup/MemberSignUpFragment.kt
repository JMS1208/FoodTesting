package com.capstone.foodtesting.ui.member.signup

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
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentMemberSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
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

        var calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        var uYear = Calendar.YEAR
        var uMonth = Calendar.MONTH
        var uDay = Calendar.DAY_OF_MONTH



        binding.tvBornDate.setOnClickListener {
            val datePickerDialog=
                DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                binding.tvBornDate.text = "${myear}년 ${mmonth+1}월 ${mdayOfMonth}일"
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
            //검사
            val noticeText = when (checkEmptyInfo()) {
                SignUpNotice.NAME_EMPTY -> {
                    "이름을 입력해주세요"
                }
                SignUpNotice.GENDER_EMPTY -> {
                    "성별을 선택해주세요"
                }
                SignUpNotice.NICKNAME_EMPTY -> {
                    "닉네임을 입력해주세요"
                }
                SignUpNotice.EMAIL_EMPTY -> {
                    "이메일을 입력해주세요"
                }
                SignUpNotice.EMAIL_PATTERN_ERROR -> {
                    "이메일 형식을 확인해주세요"
                }
                SignUpNotice.BORN_DATE_EMPTY -> {
                    "생년월일을 선택해주세요"
                }
                SignUpNotice.PASSWORD_EMPTY -> {
                    "비밀번호를 입력해주세요"
                }
                SignUpNotice.EVERYTHING_IS_OKAY -> {
                    val member = createMember()

                    viewModel.registerUserInfo(member)

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

        viewModel.mutableSignUpSuccess.observe(viewLifecycleOwner) { member->
            member?.let {
                viewModel.insertMember(it)

                setFragmentResult("SignUp", Bundle().apply {
                    putBoolean("SignUp", true)
                })

                findNavController().popBackStack()

            }

        }

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

    private fun createMember(): Member {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

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