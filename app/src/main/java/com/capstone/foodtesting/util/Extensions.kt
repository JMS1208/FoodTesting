package com.capstone.foodtesting.util

import android.app.Activity
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.amar.library.ui.StickyScrollView
import java.util.Collections.replaceAll


fun LottieAnimationView.sandboxAnimations() {
    try {
        val field = javaClass.getDeclaredField("lottieDrawable")
        field.isAccessible = true
        val lottieDrawable = (field.get(this) as LottieDrawable)
        val clazz = Class.forName(LottieDrawable::class.qualifiedName)
        val systemAnimationsEnabled = clazz.getDeclaredField("systemAnimationsEnabled")
        systemAnimationsEnabled.isAccessible = true
        systemAnimationsEnabled.set(lottieDrawable, true)
    } catch (ex: Exception) {

    }
}

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



fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30) {

        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val window = dialogFragment.dialog?.window

        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()
        window?.setLayout(x, y)

    } else {

        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialogFragment.dialog?.window

        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()

        window?.setLayout(x, y)
    }
}

fun Context.dialogResize(dialog: Dialog, width: Float, height: Float){
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30){
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val window = dialog.window

        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()

        window?.setLayout(x, y)

    }else{
        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialog.window
        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()

        window?.setLayout(x, y)
    }

}

fun androidx.core.widget.NestedScrollView.scrollToView(view: View) {
    this.smoothScrollTo(view.x.toInt(), view.y.toInt())
}

fun String.emailMasking(): String {
    val regex = """(?:\G(?!^)|(?<=^[^@]{2}|@))[^@](?!\.[^.]+$)""".toRegex()
    return this.replace(regex, "*")
}
