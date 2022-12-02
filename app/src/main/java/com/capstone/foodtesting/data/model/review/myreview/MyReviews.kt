package com.capstone.foodtesting.data.model.review.myreview


import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyReviews(
    @field:SerializedName("answer")
    val answerList: List<Answer>?,
    @field:SerializedName("customer")
    val memberUUID: String?,
    @field:SerializedName("market_info")
    val restaurant: Restaurant?
)