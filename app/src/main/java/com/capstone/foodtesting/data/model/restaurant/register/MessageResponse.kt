package com.capstone.foodtesting.data.model.restaurant.register

import android.os.Parcelable
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
@kotlinx.parcelize.Parcelize
data class MessageResponse(
    @field:SerializedName("MESSAGE")
    val message: String
): Parcelable


