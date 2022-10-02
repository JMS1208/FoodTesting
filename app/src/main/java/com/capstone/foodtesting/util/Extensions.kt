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

fun Context.statusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Context.navigationHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}
