package com.capstone.foodtesting.data.model.kakao.search.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Address(
    @field:Json(name = "address_name")
    val addressName: String?,
    @field:Json(name = "b_code")
    val bCode: String?,
    @field:Json(name = "h_code")
    val hCode: String?,
    @field:Json(name = "main_address_no")
    val mainAddressNo: String?,
    @field:Json(name = "mountain_yn")
    val mountainYn: String?,
    @field:Json(name = "region_1depth_name")
    val region1depthName: String?,
    @field:Json(name = "region_2depth_name")
    val region2depthName: String?,
    @field:Json(name = "region_3depth_h_name")
    val region3depthHName: String?,
    @field:Json(name = "region_3depth_name")
    val region3depthName: String?,
    @field:Json(name = "sub_address_no")
    val subAddressNo: String?,
    @field:Json(name = "x")
    val x: String?,
    @field:Json(name = "y")
    val y: String?
)