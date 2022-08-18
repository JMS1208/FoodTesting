package com.capstone.foodtesting.data.db

import androidx.room.TypeConverter
import java.util.*

class OrmConverter {

    @TypeConverter
    fun fromDate(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }
}