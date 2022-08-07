package com.capstone.foodtesting.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class UnsplashResponse(
    @Json(name = "results")
    val results: List<Result>?,
    @Json(name = "total")
    val total: Int?,
    @Json(name = "total_pages")
    val totalPages: Int?
)