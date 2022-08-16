package com.capstone.foodtesting.data.model.unsplash


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?
)