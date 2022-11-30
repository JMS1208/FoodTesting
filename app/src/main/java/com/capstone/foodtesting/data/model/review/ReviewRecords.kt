package com.capstone.foodtesting.data.model.review

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewRecords (
    @field:SerializedName("review_all")
    val reviewList: List<Review>
)