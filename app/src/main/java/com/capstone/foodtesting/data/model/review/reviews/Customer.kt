package com.capstone.foodtesting.data.model.review.reviews


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Customer(
    @field:SerializedName("age")
    val age: String?,
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("gender")
    val gender: Int?,
    @field:SerializedName("nickname")
    val nickname: String?
)