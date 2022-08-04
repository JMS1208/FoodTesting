package com.capstone.foodtesting.util

import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.hide() {
    this.visibility = View.GONE
}

fun BottomNavigationView.show() {
    this.visibility = View.VISIBLE
}

fun Button.hide() {
    this.isVisible = false
}

fun Button.show() {
    this.isVisible = true
}