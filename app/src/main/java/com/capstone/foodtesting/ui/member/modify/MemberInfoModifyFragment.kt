package com.capstone.foodtesting.ui.member.modify

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentMemberInfoModifyBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.sandboxAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MemberInfoModifyFragment : Fragment() {
    private var _binding: FragmentMemberInfoModifyBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MemberInfoModifyViewModel>()

//    private lateinit var member: Member

    private val calendar = java.util.Calendar.getInstance()

    private val args by navArgs<MemberInfoModifyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInfoModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lvMember.apply {
            sandboxAnimations()
            playAnimation()
        }


        initViewContents(args.member)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnModify.setOnClickListener {
            val beforePassword = args.member.password
            val password = binding.etPassword.text.toString()
            val passwordCheck = binding.etPasswordCheck.text.toString()

            if (password.isEmpty() && passwordCheck.isNotEmpty()) {
                showToast(requireContext(), "비밀번호를 입력해주세요")
                return@setOnClickListener
            }

            if (passwordCheck.isEmpty() && password.isNotEmpty()) {
                showToast(requireContext(), "비밀번호 확인을 위해 한번더 입력해주세요")
                return@setOnClickListener
            }

            if (password.isNotEmpty() && passwordCheck.isNotEmpty()) {
                if (password == passwordCheck) {
                    args.member.password = password
                } else {
                    showToast(requireContext(), "입력하신 비밀번호 두 개가 일치하지 않습니다")
                    return@setOnClickListener
                }
            }


            viewLifecycleOwner.lifecycleScope.launch {

                val result = viewModel.updateUserInfo(args.member)

                if (result.isSuccessful) {
                    result.body()?.let {
                        viewModel.insertMember(it)
                        findNavController().popBackStack()
                        showToast(requireContext(), "회원정보가 정상적으로 수정되었습니다")
                    }

                } else {
                    showToast(requireContext(), "다시 시도해주세요")
                    args.member.password = beforePassword
                }
            }


        }


        addTextWatcherToEditText()

    }

    private fun addTextWatcherToEditText() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.member.name = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })

        binding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                args.member.nickName = text.toString()
            }

            override fun afterTextChanged(p0: Editable?) = Unit

        })


    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewContents(member: Member) {
        binding.etName.setText(member.name)
        binding.etNickname.setText(member.nickName)
        binding.tvEmail.setText(member.email)
        val birthDate = Date(member.birthDate)
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 (E)")
        val bornDateText = simpleDateFormat.format(birthDate)
        binding.tvBornDate.text = bornDateText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}