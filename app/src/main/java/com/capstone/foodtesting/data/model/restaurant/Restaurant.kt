package com.capstone.foodtesting.data.model.restaurant

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "restaurant")
@kotlinx.parcelize.Parcelize
@JsonClass(generateAdapter = true)
data class Restaurant(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("reg_num")
    var reg_num: String ,
    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("market_photo")
    var photoUrl: String? = null,
    @field:SerializedName("start_date")
    val startDate: Long = System.currentTimeMillis(),
    @field:SerializedName(value = "customer_uuid")
    var customer_id: UUID? = null,
    @field:SerializedName("location")
    var address: String? = null,
    @field:SerializedName("street_loc")
    var roadAddress: String? = null,
    @field:SerializedName("period")
    var period: Long = System.currentTimeMillis(),
    @field:SerializedName("longtitude")
    var longitude: Double? = null,
    @field:SerializedName("latitude")
    var latitude: Double? = null,
    @field:SerializedName("category")
    var category: String? = null,
    @field:SerializedName("open_time")
    var openTime: Long? = null,
    @field:SerializedName("close_time")
    var closeTime: Long? = null,
    @field:SerializedName("congestion_degree")
    var complexity: String? = null,
    @field:SerializedName("holiday")
    var holiday: String? = null,
    @field:SerializedName("phone_num")
    var telephoneNumber: String? = null,
//    var queryLines: List<QueryLine> = emptyList(),
): Parcelable