package com.capstone.foodtesting.util

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyValueFormatter(
    val unit: String = ""
) : ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        val mFormat = DecimalFormat("###,###,##0")
        return mFormat.format(value.toInt())+" $unit"
    }
}

