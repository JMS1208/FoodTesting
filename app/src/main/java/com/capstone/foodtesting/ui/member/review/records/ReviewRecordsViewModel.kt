package com.capstone.foodtesting.ui.member.review.records

import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.review.myreview.MyReviewResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.Response


@HiltViewModel
class ReviewRecordsViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    suspend fun getMyReviews(customerUUID: String): Response<MyReviewResponse> {
        return repository.getMyReviews(customerUUID)
    }

}