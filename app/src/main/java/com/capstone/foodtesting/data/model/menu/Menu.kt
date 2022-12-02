package com.capstone.foodtesting.data.model.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class Menu(
    @field:SerializedName("post_uuid")
    val post_uuid: String = UUID.randomUUID().toString(),
    @field:SerializedName("subject")
    var menuName: String ?= null,
    @field:SerializedName("write_market")
    var regNumber: String,
    @field:SerializedName("writer_uuid")
    var customer_uuid: String,
    @field:SerializedName("hope_price")
    var hope_price: Int? = null,
    @field:SerializedName("discounted_price")
    var discount_price: Int? = null,
    @field:SerializedName("start_date")
    var start_date: Long? = null,
    @field:SerializedName("end_date")
    var end_date: Long? = null,
    @field:SerializedName("ingredients")
    var ingredients: String? = null,
    @field:SerializedName("post_date")
    var post_date: Long? = System.currentTimeMillis(),
    @field:SerializedName("update_date")
    var update_date: Long? = post_date,
    @field:SerializedName("contents")
    var explains: String? = null,
    @field:SerializedName("menu_photo")
    var photoUrl: String? = null,
    @field:SerializedName("is_break")
    var is_break: Int = 0

): Parcelable