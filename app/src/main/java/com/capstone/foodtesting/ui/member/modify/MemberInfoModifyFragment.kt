package com.capstone.foodtesting.ui.member.modify

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentMemberInfoModifyBinding
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.sandboxAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MemberInfoModifyFragment: Fragment(){
    private var _binding: FragmentMemberInfoModifyBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MemberInfoModifyViewModel>()

    private lateinit var member: Member

    private val calendar = java.util.Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInfoModifyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lvMember.apply {
            sandboxAnimations()
            playAnimation()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMember.collectLatest { member->
                member?.let {
                    this@MemberInfoModifyFragment.member = member
                    initViewContents(it)
                }

            }
        }

        binding.btnClose.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnModify.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = viewModel.updateUserInfo(member)

                if (result.isSuccessful) {
                    result.body()?.let {
                        viewModel.insertMember(it)
                        findNavController().popBackStack()
                        showToast(requireContext(), "회원정보가 정상적으로 수정되었습니다")
                    }

                } else {
                    showToast(requireContext(), "다시 시도해주세요")
                }
            }
        }




    }

    @SuppressLint("SimpleDateFormat")
    private fun initViewContents(member: Member) {
        binding.etName.setText(member.name)
        binding.etNickname.setText(member.nickName)
        binding.etEmail.setText(member.email)
        val birthDate = Date(member.birthDate)
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 (E)")
        val bornDateText = simpleDateFormat.format(birthDate)
        binding.tvBornDate.text = bornDateText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}