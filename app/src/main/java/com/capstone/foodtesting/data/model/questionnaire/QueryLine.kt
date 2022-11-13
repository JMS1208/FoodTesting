package com.capstone.foodtesting.data.model.questionnaire

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class QueryLine(
    @field:SerializedName("uuid")
    val uuid: UUID = UUID.randomUUID(),
    @field:SerializedName("reg_num")
    val reg_num: String?=null,
    @field:SerializedName("query")
    val query: String,
    @field:SerializedName("keywords")
    val keywords: List<String>? = null,
    @field:SerializedName("type")
    val queryType: Int
): Parcelable {

    companion object QueryType{
        const val TypeRestaurant = 0
        const val TypeMenu = 1
        const val TypeAdd = 2
    }

}
