package com.capstone.foodtesting.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "id")
    val id: String?,
    @Json(name = "urls")
    val urls: Urls?,
    @Json(name = "user")
    val user: User?
)