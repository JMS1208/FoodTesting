package com.capstone.foodtesting.data.model.review.reviews


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answer(
    @field:SerializedName("contents")
    val contents: String?,
    @field:SerializedName("review_date")
    val reviewDate: Long?,
    @field:SerializedName("review_line")
    val reviewLine: String?
)