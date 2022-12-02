package com.capstone.foodtesting.data.model.kakao.local


import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class KakaoLocalResponse(
    @field:Json(name = "documents")
    @Embedded
    val addressInfos: List<AddressInfo?>?,
    @field:Json(name = "meta")
    @Embedded
    val meta: Meta?
)