package com.capstone.foodtesting.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member

@Database(
    entities = [AddressInfo::class, Member::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class FoodTestingDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun memberDao(): MemberDao
}