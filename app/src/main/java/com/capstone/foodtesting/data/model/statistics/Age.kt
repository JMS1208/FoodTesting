package com.capstone.foodtesting.data.model.statistics


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Age(
    @field:SerializedName("10")
    val age_10: Int?,
    @field:SerializedName("20")
    val age_20: Int?,
    @field:SerializedName("30")
    val age_30: Int?,
    @field:SerializedName("40")
    val age_40: Int?,
    @field:SerializedName("50")
    val age_50: Int?
)