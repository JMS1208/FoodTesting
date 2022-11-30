package com.capstone.foodtesting.data.model.questionnaire

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class QueryLineList (
    @field:SerializedName("ques")
    val queryLineList: List<QueryLine>
): Parcelable