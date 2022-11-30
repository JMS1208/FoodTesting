package com.capstone.foodtesting.data.model.file.process

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ImageHashResponse(
    @field:SerializedName("MESSAGE")
    val imageHash: String?
 ): Parcelable

