package com.capstone.foodtesting.data.model.restaurant.home


import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewRestaurantList(
    @field:SerializedName("markets")
    val markets: List<Restaurant>?
)