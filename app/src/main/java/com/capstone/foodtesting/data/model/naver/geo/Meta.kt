package com.capstone.foodtesting.data.model.naver.geo


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @field:SerializedName("count")
    val count: Int?,
    @field:SerializedName("page")
    val page: Int?,
    @field:SerializedName("totalCount")
    val totalCount: Int?
)