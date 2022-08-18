package com.capstone.foodtesting.data.model.kakao.search.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Meta(
    @field:Json(name = "is_end")
    val isEnd: Boolean?,
    @field:Json(name = "pageable_count")
    val pageableCount: Int?,
    @field:Json(name = "total_count")
    val totalCount: Int?
)