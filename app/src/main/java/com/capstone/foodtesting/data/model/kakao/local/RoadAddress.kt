package com.capstone.foodtesting.data.model.kakao.local


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class RoadAddress(
    @field:Json(name = "address_name")
    val roadAddressFullName: String?,
    @field:Json(name = "building_name")
    val buildingName: String?,
    @field:Json(name = "main_building_no")
    val mainBuildingNo: String?,
    @field:Json(name = "region_1depth_name")
    val roadRegion1depthName: String?,
    @field:Json(name = "region_2depth_name")
    val roadRegion2depthName: String?,
    @field:Json(name = "region_3depth_name")
    val roadRegion3depthName: String?,
    @field:Json(name = "road_name")
    val roadName: String?,
    @field:Json(name = "sub_building_no")
    val subBuildingNo: String?,
    @field:Json(name = "underground_yn")
    val undergroundYn: String?,
    @field:Json(name = "zone_no")
    val zoneNo: String?
)