package com.capstone.foodtesting.data.model.unsplash


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "small")
    val small: String?
)