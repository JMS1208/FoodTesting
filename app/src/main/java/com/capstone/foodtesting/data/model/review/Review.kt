package com.capstone.foodtesting.data.model.review

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@kotlinx.parcelize.Parcelize
data class Review (
    val uuid: UUID = UUID.randomUUID(),
    val writer_uuid: UUID,
    var RestaurantRegNumber: String,
    var query_uuid: UUID,
    var post_date: Long,
    var contents: String,
        ): Parcelable {
}