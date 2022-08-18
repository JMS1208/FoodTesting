package com.capstone.foodtesting.data.model.kakao.local


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Meta(
    @field:Json(name = "total_count")
    val totalCount: Int?
)