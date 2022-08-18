package com.capstone.foodtesting.util

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.capstone.foodtesting.R
import dagger.hilt.android.qualifiers.ApplicationContext
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip
import javax.inject.Inject

object CommonFunc {

    fun showTooltip(context: Context,
                    view: View,
                    text: String,
                    arrowShowing: Boolean = true,
                    styleId: Int? = R.style.ToolTipAltStyle,
                    floatingAnimation: Tooltip.Animation? = null
    ) {
        try {
            val tooltip = Tooltip.Builder(context)
                .anchor(view, 0, 0, true)
                .text(text)
                .typeface(Typeface.DEFAULT)
                .arrow(arrowShowing)
                .closePolicy(ClosePolicy.TOUCH_OUTSIDE_CONSUME)
                .styleId(styleId)
                .floatingAnimation(floatingAnimation)
                .showDuration(10000L)
                .overlay(false)
                .create()

            tooltip.show(view, Tooltip.Gravity.TOP, true)
        } catch(E: Exception) {
            Log.d("TAG", "showTooltip ${E.message}")
        }


    }
}