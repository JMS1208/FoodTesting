package com.capstone.foodtesting.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MonthValueFormatter: IndexAxisValueFormatter() {

    private val ages = arrayOf("1월", "2월", "3월", "4월", "5월", "6월","7월", "8월", "9월", "10월", "11월", "12월")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return ages.getOrNull(value.toInt() - 1) ?: value.toString()
    }
}