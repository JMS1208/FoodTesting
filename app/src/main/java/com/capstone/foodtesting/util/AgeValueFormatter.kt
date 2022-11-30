package com.capstone.foodtesting.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat


class AgeValueFormatter : IndexAxisValueFormatter() {

    private val ages = arrayOf("10대", "20대", "30대", "40대", "50대 이상")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return ages.getOrNull(value.toInt()/10 - 1) ?: value.toString()
    }
}