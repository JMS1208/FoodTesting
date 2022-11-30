package com.capstone.foodtesting.data.model.statistics


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PerMonth(
    @field:SerializedName("1")
    val month_1: Int?,
    @field:SerializedName("2")
    val month_2: Int?,
    @field:SerializedName("3")
    val month_3: Int?,
    @field:SerializedName("4")
    val month_4: Int?,
    @field:SerializedName("5")
    val month_5: Int?,
    @field:SerializedName("6")
    val month_6: Int?,
    @field:SerializedName("7")
    val month_7: Int?,
    @field:SerializedName("8")
    val month_8: Int?,
    @field:SerializedName("9")
    val month_9: Int?,
    @field:SerializedName("10")
    val month_10: Int?,
    @field:SerializedName("11")
    val month_11: Int?,
    @field:SerializedName("12")
    val month_12: Int?
)