package com.capstone.foodtesting.data.model.member


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestingItem(
    @Json(name = "born_date")
    val bornDate: String?,
    @Json(name = "customer_type")
    val customerType: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "password")
    val password: String?,
    @Json(name = "uuid")
    val uuid: String?
)