package com.capstone.foodtesting.data.model.review

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@kotlinx.parcelize.Parcelize
data class Review (
    @field:SerializedName("review_uuid")
    val uuid: UUID = UUID.randomUUID(),
    @field:SerializedName("writer_uuid")
    val writer_uuid: UUID,
    @field:SerializedName("market_reg_num")
    var RestaurantRegNumber: String,
    @field:SerializedName("ques_uuid")
    var query_uuid: UUID,
    @field:SerializedName("review_date")
    var post_date: Long,
    @field:SerializedName("review_line")
    var contents: String,
        ): Parcelable {
}