package com.capstone.foodtesting.ui.member.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.databinding.FragmentMemberReviewBinding
import com.capstone.foodtesting.util.CommonFunc.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MemberReviewFragment : Fragment() {

    private var _binding: FragmentMemberReviewBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MemberReviewFragmentArgs>()

    private val viewModel by viewModels<MemberReviewViewModel>()

    //임시로 만든거
    private lateinit var queryList: List<QueryLine>

    lateinit var answerList: List<Review>

    private lateinit var viewPagerAdapter: FragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberReviewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뷰모델에 args 전달해서 요청하면 나오게
        viewLifecycleOwner.lifecycleScope.launch {
            val result = viewModel.getRestaurantQuestions(args.regNum)
            viewModel.getMemberInfo.collectLatest { member ->
                member?.let {
                    if (result.isSuccessful) {
                        result.body()?.let { queryList->
                            this@MemberReviewFragment.queryList = queryList
                            answerList = List(queryList.size) { idx->
                                val query = queryList[idx]
                                Review(UUID.randomUUID(), member.uuid, query.reg_num!!, query.uuid, System.currentTimeMillis(), "" )
                            }
                            setupViewPager2()

                        }
                    } else {
                        findNavController().popBackStack()
                        showToast(requireContext(),"네트워크를 확인해주세요")
                    }
                }
            }

        }


        binding.tvLeft.setOnClickListener {
            if (this::queryList.isInitialized) {
                saveQueryAnswer()
                if(binding.viewPager2.currentItem > 0) {

                    binding.viewPager2.currentItem -= 1

                } else {
                    findNavController().popBackStack()
                }
            } else {
                findNavController().popBackStack()
            }

        }

        binding.tvRight.setOnClickListener {
            if (this::queryList.isInitialized) {
                saveQueryAnswer()
                if (binding.viewPager2.currentItem < queryList.lastIndex) {

                    binding.viewPager2.currentItem += 1
                } else {

                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = viewModel.postReview(answerList)

                        if (result.isSuccessful) {
                            showToast(requireContext(), "사장님께 피드백을 성공적으로 전달하였습니다 !")
                            findNavController().popBackStack()
                        }
                    }

                }
            }

        }




    }


    private fun setupViewPager2() {
        viewPagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = queryList.size

            override fun createFragment(position: Int): Fragment {
                val queryLine = queryList[position]
                val fragment = MemberReviewContentsFragment().apply {
                    setQueryLineInFragment(queryLine)
                }
                return fragment
            }

        }


        binding.viewPager2.adapter = viewPagerAdapter
        binding.viewPager2.isUserInputEnabled = false

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> {
                        binding.tvLeft.text = "취소"
                        binding.tvRight.text = "(${position+1} / ${queryList.size}) 다음"
                    }
                    queryList.size-1 -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "작성완료"
                    }
                    else -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "(${position+1} / ${queryList.size}) 다음"
                    }

                }
            }
        })
    }

    private fun saveQueryAnswer() {
        val position = binding.viewPager2.currentItem
        val fragment: MemberReviewContentsFragment = viewPagerAdapter.createFragment(position) as MemberReviewContentsFragment
        fragment.saveAnswer(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}