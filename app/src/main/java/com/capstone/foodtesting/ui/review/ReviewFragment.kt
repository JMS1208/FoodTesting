package com.capstone.foodtesting.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.capstone.foodtesting.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ReviewFragmentArgs>()

    //임시로 만든거
    private val fragmentList = arrayOf(
        ReviewLineFragment(),
        ReviewLineFragment(),
        ReviewLineFragment(),
        ReviewLineFragment(),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뷰모델에 args 전달해서 요청하면 나오게

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        binding.viewPager2.isUserInputEnabled = false

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> {
                        binding.tvLeft.text = "취소"
                        binding.tvRight.text = "(${position+1} / ${fragmentList.size}) 다음"
                    }
                    fragmentList.size-1 -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "작성완료"
                    }
                    else -> {
                        binding.tvLeft.text = "이전"
                        binding.tvRight.text = "(${position+1} / ${fragmentList.size}) 다음"
                    }

                }
            }
        })

        binding.tvLeft.setOnClickListener {
            if(binding.viewPager2.currentItem > 0) {
                binding.viewPager2.currentItem -= 1
            } else {
                findNavController().popBackStack()
            }

        }

        binding.tvRight.setOnClickListener {
            if (binding.viewPager2.currentItem < fragmentList.lastIndex) {
                binding.viewPager2.currentItem += 1
            } else {
                findNavController().popBackStack()
            }



        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}