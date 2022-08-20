package com.capstone.foodtesting.ui.info

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.databinding.FragmentInfoBinding
import com.capstone.foodtesting.ui.bottomsheet.setaddress.BSSetupAddrFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InfoFragment : Fragment() {

    private var _binding : FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<InfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestAddressInfo.collectLatest { addressInfo->
                    binding.tvAddress.text = addressInfo?.address?.addressFullName ?: "주소 설정하러 가기"
                }

            }
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.tvAddress.setOnClickListener {
            val bottomSheet = BSSetupAddrFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.tvManagePosting.setOnClickListener {

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMemberInfo.collectLatest { memberInfo->
                    memberInfo?.let {
                        val nickAndType = "${it.nickName}(${ if(it.type == Member.TYPE_TESTER) "테스터" else "사장님" })"
                        binding.tvNickName.text = nickAndType
                        binding.tvEmail.text = it.email
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}