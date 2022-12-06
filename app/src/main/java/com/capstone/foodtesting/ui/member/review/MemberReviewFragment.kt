package com.capstone.foodtesting.ui.member.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentMemberReviewBinding
import com.capstone.foodtesting.util.CommonFunc
import com.capstone.foodtesting.util.CommonFunc.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemberReviewFragment : Fragment() {

    private var _binding: FragmentMemberReviewBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MemberReviewFragmentArgs>()

    val viewModel by viewModels<MemberReviewViewModel>()

//    private var member: Member? = null

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
            try {
                viewModel.fetchRestaurantQuestions(args.regNum)

            } catch (E: Exception) {
                E.printStackTrace()
                findNavController().popBackStack()
                CommonFunc.showToast(requireContext(), "다음에 다시 시도해주세요")
            }

        }

        viewModel.restaurantQueryList.observe(viewLifecycleOwner) { queryList->
            queryList?.let {
                setupViewPager2(it)

                viewModel.makeInitReview(args.regNum, args.member, it)


            }
        }


        binding.tvLeft.setOnClickListener {


            val queryList = viewModel.restaurantQueryList.value

            queryList?.let {
                if(it.isEmpty()) {
                    showToast(requireContext(), "현재 등록된 질문이 없습니다")
                    findNavController().popBackStack()
                }

                if(binding.viewPager2.currentItem > 0 ) {
//                    saveReview()
                    binding.viewPager2.currentItem -= 1
                } else {
                    findNavController().popBackStack()
                }

            }



        }

        binding.tvRight.setOnClickListener {

            val queryList = viewModel.restaurantQueryList.value



            queryList?.let {

                if(it.isEmpty()) {
                    showToast(requireContext(), "현재 등록된 질문이 없습니다")
                    findNavController().popBackStack()
                }

                if(binding.viewPager2.currentItem < it.lastIndex) {
//                    saveReview()
                    binding.viewPager2.currentItem += 1
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val isSuccessful = viewLifecycleOwner.lifecycleScope.async {
                            val reviews = viewModel.userReviewList.value?: emptyList()

                            reviews.map { review->
                                if(review.contents.isEmpty()) {
                                    review.contents = " "
                                }
                            }

                            val result = viewModel.postReview(reviews)

                            result.isSuccessful
                        }

                        if (isSuccessful.await()) {
                            CommonFunc.showToast(requireContext(), "사장님께 피드백을 성공적으로 전달하였습니다 !")

                            findNavController().popBackStack()
                        } else {
                            CommonFunc.showToast(requireContext(), "피드백 전달에 실패하였습니다 ")

                        }
                    }
                }
            } ?: CommonFunc.showToast(requireContext(), "현재 등록된 질문이 없습니다")



        }


    }

//    private fun saveReview() {
//        val idx = binding.viewPager2.currentItem
//
//        viewModel.userReviewList.value?.apply {
//            this[idx].contents =
//        }
//    }

    fun getCurrentPage(): Int {
        return binding.viewPager2.currentItem
    }


    private fun setupViewPager2(queryList: List<QueryLine>) {
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

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.tvLeft.text = "취소"
                        binding.tvRight.text = "(${position + 1} / ${queryList.size}) 다음"
                    }
                    queryList.size - 1 -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "작성완료"
                    }
                    else -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "(${position + 1} / ${queryList.size}) 다음"
                    }

                }
            }
        })
    }

//    private fun saveQueryAnswer() {
//        val position = binding.viewPager2.currentItem
//        val fragment: MemberReviewContentsFragment =
//            viewPagerAdapter.createFragment(position) as MemberReviewContentsFragment
//        fragment.saveAnswer(position)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}