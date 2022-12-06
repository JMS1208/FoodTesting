package com.capstone.foodtesting.ui.restaurant.review

import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.model.review.QuesAnswer
import com.capstone.foodtesting.data.model.review.reviews.ReviewsForRestaurantResponse
import com.capstone.foodtesting.data.model.statistics.ReviewStatistics
import com.capstone.foodtesting.data.model.statistics.ReviewStatisticsResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ReviewAnalViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    suspend fun getReviewStatistics(
        reg_num: String
    ): Response<ReviewStatisticsResponse> {
        return repository.getReviewStatistics(reg_num)
    }

    suspend fun getRestaurantInfoByRegNum(
        reg_num: String
    ): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByRegNum(reg_num)
    }

    suspend fun getReviewsForRestaurant(
        reg_num: String
    ): Response<ReviewsForRestaurantResponse> {
        return repository.getReviewsForRestaurant(reg_num)
    }

    suspend fun getReviewSummary(
        reg_num: String
    ): Response<MessageResponse> {
        return repository.getReviewSummary(reg_num)
    }

}