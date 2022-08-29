package com.capstone.foodtesting.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.amar.library.ui.StickyScrollView


fun StickyScrollView.scrollToView(view: View) {
    this.smoothScrollTo(view.x.toInt(), view.y.toInt()-10)
}

fun StickyScrollView.computeDistanceToView(view: View): Int {
    return (view.y - this.y).toInt()
}

