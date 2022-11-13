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
    var menuName: String,
    @field:SerializedName("write_market_id")
    var regNumber: String,
    @field:SerializedName("writer_uuid_id")
    var customer_uuid: String,
    @field:SerializedName("hope_price")
    var hope_price: Int,
    @field:SerializedName("discounted_price")
    var discount_price: Int,
    @field:SerializedName("start_date")
    var start_date: Long,
    @field:SerializedName("end_date")
    var end_date: Long,
    @field:SerializedName("ingredients")
    var ingredients: String,
    @field:SerializedName("post_date")
    var post_date: Long,
    @field:SerializedName("update_date")
    var update_date: Long = post_date,
    @field:SerializedName("contents")
    var explains: String,
    @field:SerializedName("menu_photo")
    var photoUrl: String? = null,
    @field:SerializedName("is_break")
    var is_break: Boolean = false

): Parcelable {
}