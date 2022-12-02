package com.capstone.foodtesting.data.model.restaurant


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryRestaurant(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: Any?,
    @Json(name = "previous")
    val previous: Any?,
    @Json(name = "results")
    val results: List<Restaurant>?
)