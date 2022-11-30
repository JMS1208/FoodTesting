package com.capstone.foodtesting.data.model.review

import android.os.Parcelable
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ReviewList (
    @field:SerializedName("reviews")
    val reviewList: List<Review>
): Parcelable