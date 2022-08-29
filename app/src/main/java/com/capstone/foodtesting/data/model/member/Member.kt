package com.capstone.foodtesting.data.model.member

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capstone.foodtesting.util.DateSerializer
import com.capstone.foodtesting.util.UUIDSerializer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import java.util.*

@Entity(tableName = "member")
data class Member(
    @PrimaryKey(autoGenerate = false)
    val uuid: UUID = UUID.randomUUID(),
    val email: String,
    val name: String,
    val nickName: String,
    val gender: Int,
    val profile: String?,
    val age: Int,
    val birthDate: Date,
    var loginTime: Date = Date(System.currentTimeMillis()), // 로그인 시점
    val phoneNumber: String?,
    val social_member: Int,
    var type: Int = TYPE_TESTER
) {

    companion object {
        const val UNKNOWN=0
        const val MALE = 1
        const val FEMALE = 2

        const val KAKAO_MEMBER = 3
        const val NAVER_MEMBER = 4
        const val GOOGLE_MEMBER = 5
        const val FOOD_TESTING_MEMBER = 6

        const val TYPE_TESTER = 7
        const val TYPE_CEO = 8
    }

}