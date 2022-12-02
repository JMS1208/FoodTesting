package com.capstone.foodtesting.data.model.statistics

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewStatisticsResponse (
    @field:SerializedName("review_result")
    val reviewStatistics: ReviewStatistics?
)