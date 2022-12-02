package com.capstone.foodtesting.ui.member.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.questionnaire.QueryLineList
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response
import java.util.*

@HiltViewModel
class MemberReviewViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val restaurantQueryList: MutableLiveData<List<QueryLine>> = MutableLiveData()

    val userReviewList: MutableLiveData<MutableList<Review>> = MutableLiveData()


    fun makeInitReview(reg_num: String, member: Member, queryLineList: List<QueryLine>) {
        val reviewList = mutableListOf<Review>()
        for ( i in queryLineList.indices) {
            val review = Review(
                UUID.randomUUID(),
                member.uuid,
                reg_num,
                queryLineList[i].uuid,
                System.currentTimeMillis(),
                ""
            )
            reviewList.add(review)
        }

        userReviewList.postValue(reviewList)
    }
    fun fetchRestaurantQuestions(reg_num: String) = viewModelScope.launch(Dispatchers.IO) {

        val result = repository.getRestaurantQuestions(reg_num)

        if (result.isSuccessful) {
            result.body()?.queryLineList?.let {
                restaurantQueryList.postValue(it)

            }
        }


    }

    fun addReviewList(review: Review) {
        val reviews = userReviewList.value ?: mutableListOf()

        reviews.add(review)
    }

    fun updateReviewList(reviewList: MutableList<Review>) {
        userReviewList.postValue(reviewList)
    }


    suspend fun postReview(reviewList: List<Review>): Response<MessageResponse> {

        return repository.postReview(reviewList)
    }

    val getMemberInfo: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )
}