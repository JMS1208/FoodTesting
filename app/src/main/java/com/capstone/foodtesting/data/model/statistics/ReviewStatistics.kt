package com.capstone.foodtesting.data.model.statistics


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewStatistics(
    @field:SerializedName("age")
    val age: Age?,
    @field:SerializedName("gender")
    val gender: Gender?,
    @field:SerializedName("per_month")
    val perMonth: PerMonth?,
    @field:SerializedName("total")
    val total: Int?
)