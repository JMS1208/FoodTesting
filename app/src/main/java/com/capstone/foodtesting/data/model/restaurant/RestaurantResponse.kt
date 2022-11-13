package com.capstone.foodtesting.data.model.restaurant


import android.os.Parcelable
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@kotlinx.parcelize.Parcelize
data class RestaurantResponse(
    @field:SerializedName("market")
    val market: Restaurant?,
    @field:SerializedName("post")
    val menuList: List<Menu>?
): Parcelable