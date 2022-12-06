package com.capstone.foodtesting.data.model.review.reviews


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewAll(
    @SerializedName("answer")
    val answer: List<Answer>?,
    @SerializedName("customer")
    val customer: Customer?,
    @SerializedName("market_reg_num")
    val marketRegNum: String?
)