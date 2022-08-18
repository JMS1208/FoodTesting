package com.capstone.foodtesting.data.model.kakao.search.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class AddressSearchResponse(
    @field:Json(name = "documents")
    val documents: List<Document>?,
    @field:Json(name = "meta")
    val meta: Meta?
)