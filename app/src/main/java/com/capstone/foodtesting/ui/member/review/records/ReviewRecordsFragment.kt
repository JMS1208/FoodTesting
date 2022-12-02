package com.capstone.foodtesting.ui.member.review.records

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentReviewRecordsBinding
import com.capstone.foodtesting.util.CommonFunc.showToast
import com.capstone.foodtesting.util.sandboxAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.NullPointerException

@AndroidEntryPoint
class ReviewRecordsFragment : Fragment() {

    private var _binding: FragmentReviewRecordsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ReviewRecordsViewModel>()

    private val args by navArgs<ReviewRecordsFragmentArgs>()

    private lateinit var reviewRecordsAdapter: ReviewRecordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewRecordsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbarView()
        initLottieView()
        initReviewRecordsRecyclerView()
        loadMyReview()

    }

    private fun initReviewRecordsRecyclerView() {
        reviewRecordsAdapter = ReviewRecordsAdapter()
        reviewRecordsAdapter.setOnItemClickListener {
            //TODO
        }

        binding.rvMyReviews.apply {
            adapter = reviewRecordsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadMyReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = viewModel.getMyReviews(args.member.uuid.toString())

                if (response.isSuccessful) {

                    response.body()?.myReviews?.let {
                        reviewRecordsAdapter.submitList(it)
                    }

                }

            } catch (nullException: NullPointerException) {
                Log.e("TAG", "리뷰 로드 널포인트")
                findNavController().popBackStack()
                showToast(requireContext(), "회원 정보가 조회되지 않습니다")
            } catch (E: Exception) {
                Log.e("TAG", "loadMyReview: ${E.message} ")
                findNavController().popBackStack()
            }


        }
    }

    private fun initLottieView() {
//        binding.lvReview.apply {
//            sandboxAnimations()
//            playAnimation()
//        }
    }

    private fun initToolbarView() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}