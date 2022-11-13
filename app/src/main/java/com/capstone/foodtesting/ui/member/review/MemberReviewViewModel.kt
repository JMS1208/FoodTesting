package com.capstone.foodtesting.ui.member.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class MemberReviewViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    suspend fun getRestaurantQuestions(reg_num: String): Response<List<QueryLine>> {
        return repository.getRestaurantQuestions(reg_num)
    }

    suspend fun postReview(reviewList: List<Review>): Response<List<Review>> {
        return repository.postReview(reviewList)
    }

    val getMemberInfo: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )
}