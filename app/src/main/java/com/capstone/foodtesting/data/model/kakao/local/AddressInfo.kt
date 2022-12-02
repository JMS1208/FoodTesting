package com.capstone.foodtesting.data.model.kakao.local


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.capstone.foodtesting.util.DateSerializer
import com.capstone.foodtesting.util.UUIDSerializer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Entity(tableName = "address_info")
@Serializable
@JsonClass(generateAdapter = true)
data class AddressInfo(
    @Serializable(with = UUIDSerializer::class)
    @field:Json(ignore = true)
    @PrimaryKey(autoGenerate = false)
    val uuid: UUID = UUID.randomUUID(),
    @Serializable(with = DateSerializer::class)
    @field:Json(ignore = true)
    var date: Date = Date(System.currentTimeMillis()),
    @field:Json(name = "address")
    @Embedded
    val address: Address?,
    @field:Json(name = "road_address")
    @Embedded
    val roadAddress: RoadAddress?,
    var x: String? = null,
    var y: String? = null
) {
    @field:Json(ignore = true)
    @Ignore
    var isFirstItem: Boolean = false
}