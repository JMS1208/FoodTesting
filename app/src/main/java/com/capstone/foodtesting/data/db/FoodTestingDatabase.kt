package com.capstone.foodtesting.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.Restaurant

@Database(
    entities = [AddressInfo::class, Member::class, Restaurant::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class FoodTestingDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun memberDao(): MemberDao

    abstract fun favoriteRestaurantDao(): FavoriteRestaurantDao
}