package com.capstone.foodtesting.data.model.review.reviews


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewsForRestaurantResponse(
    @SerializedName("review_all")
    val reviewAll: List<ReviewAll>?
)