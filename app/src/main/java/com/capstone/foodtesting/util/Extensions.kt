package com.capstone.foodtesting.util

import android.view.View
import com.amar.library.ui.StickyScrollView


fun StickyScrollView.scrollToView(view: View) {
    this.smoothScrollTo(view.x.toInt(), view.y.toInt())
}

fun StickyScrollView.computeDistanceToView(view: View): Int {
    return (view.y - this.y).toInt()
}