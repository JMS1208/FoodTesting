package com.capstone.foodtesting.data.model.date

import android.os.Parcelable

import java.util.*

@kotlinx.parcelize.Parcelize
data class DateTime(
    val whenOption : WHEN ,
    var year: Int = Calendar.getInstance().get(Calendar.YEAR),
    var month: Int = Calendar.getInstance().get(Calendar.MONTH),
    var day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    var hour: Int = Calendar.getInstance().get(Calendar.HOUR),
    var minute: Int = Calendar.getInstance().get(Calendar.MINUTE)
): Parcelable {


    enum class WHEN {
        START,
        END
    }

    fun getDateTime(): Date {
        return GregorianCalendar(year,month,day,hour,minute).time
    }
}