package com.capstone.foodtesting.data.model.questionnaire

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class QueryLine(
    @field:SerializedName("ques_uuid")
    val uuid: UUID = UUID.randomUUID(),
    @field:SerializedName("market_reg_num")
    var reg_num: String? = null,
    @field:SerializedName("contents")
    val query: String,
    @field:SerializedName("fast_response")
    val keywords: List<String>? = null,
    @field:SerializedName("ques_type")
    val queryType: Int
): Parcelable {

    companion object QueryType{
        const val TypeRestaurant = 0
        const val TypeMenu = 1
        const val TypeAdd = 2
    }

}
