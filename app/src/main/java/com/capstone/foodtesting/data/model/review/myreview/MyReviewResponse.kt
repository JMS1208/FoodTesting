package com.capstone.foodtesting.data.model.review.myreview


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyReviewResponse(
    @field:SerializedName("review_all")
    val myReviews: List<MyReviews>?
)