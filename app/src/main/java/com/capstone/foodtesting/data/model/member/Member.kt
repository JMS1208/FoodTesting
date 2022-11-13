package com.capstone.foodtesting.data.model.member

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.capstone.foodtesting.util.DateSerializer
import com.capstone.foodtesting.util.UUIDSerializer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Entity(tableName = "member")
@Parcelize
data class Member(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("uuid")
    val uuid: UUID = UUID.randomUUID(),
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String?, //소셜로그인의 경우 null
    @field:SerializedName("name")
    val name: String?,
    @field:SerializedName("nickname")
    val nickName: String?,
    @field:SerializedName("gender")
    val gender: Int,
//    val profile: String? = null,
//    val age: Int,
    @field:SerializedName("born_date")
    val birthDate: Long,
    var loginTime: Long? = System.currentTimeMillis(), // 로그인 시점
//    val phoneNumber: String?,
    @field:SerializedName("is_social")
    val social_member: Int = FOOD_TESTING_MEMBER,
    @field:SerializedName("customer_type")
    var type: Int = TYPE_TESTER
): Parcelable {

    companion object {
        const val UNKNOWN=0
        const val MALE = 1
        const val FEMALE = 2

        const val KAKAO_MEMBER = 0
        const val NAVER_MEMBER = 1
        const val GOOGLE_MEMBER = 2
        const val FOOD_TESTING_MEMBER = 3

        const val TYPE_NONE = 0
        const val TYPE_TESTER = 1
        const val TYPE_CEO = 2
    }

}