package com.capstone.foodtesting.data.model.review.myreview


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answer(
    @field:SerializedName("contents")
    val question: String?,
    @field:SerializedName("ques_uuid")
    val quesUuid: String?,
    @field:SerializedName("review_date")
    val reviewDate: Long?,
    @field:SerializedName("review_line")
    val answer: String?,
    @field:SerializedName("review_uuid")
    val reviewUuid: String?
)