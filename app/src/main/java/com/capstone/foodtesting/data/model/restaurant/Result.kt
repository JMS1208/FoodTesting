package com.capstone.foodtesting.data.model.restaurant


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "category")
    val category: String?,
    @Json(name = "close_time")
    val closeTime: Any?,
    @Json(name = "congestion_degree")
    val congestionDegree: String?,
    @Json(name = "customer_uuid")
    val customerUuid: String?,
    @Json(name = "holiday")
    val holiday: Any?,
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "longtitude")
    val longtitude: Double?,
    @Json(name = "market_photo")
    val marketPhoto: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "open_time")
    val openTime: Int?,
    @Json(name = "period")
    val period: Int?,
    @Json(name = "phone_num")
    val phoneNum: String?,
    @Json(name = "reg_num")
    val regNum: String?,
    @Json(name = "start_date")
    val startDate: Int?,
    @Json(name = "street_loc")
    val streetLoc: Any?
)