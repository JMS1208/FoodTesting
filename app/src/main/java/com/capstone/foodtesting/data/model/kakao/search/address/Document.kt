package com.capstone.foodtesting.data.model.kakao.search.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Document(
    @field:Json(ignore = true)
    var query: String? = null,
    @field:Json(name = "address")
    val address: Address?,
    @field:Json(name = "address_name")
    val addressName: String?,
    @field:Json(name = "address_type")
    val addressType: String?,
    @field:Json(name = "road_address")
    val roadAddress: RoadAddress?,
    @field:Json(name = "x")
    val x: String?,
    @field:Json(name = "y")
    val y: String?
)