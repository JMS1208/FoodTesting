package com.capstone.foodtesting.util

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.capstone.foodtesting.R
import com.skydoves.balloon.*

object CommonFunc {

    fun createLottieView(context: Context, assetName: String): LottieAnimationView {
        val lottieView = LottieAnimationView(context)

        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1f)
        lottieView.apply {
            setAnimation(assetName)
            sandboxAnimations()
            layoutParams = lp
        }
        return lottieView

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showTooltip(context: Context,
                    view: View,
                    text: String,
                    lifecycleOwner: LifecycleOwner? = null,
                    backgroundColorId: Int = R.color.base_blue

    ) {
        try {

            val iconForm = IconForm.Builder(context)
                .setDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info))
                .setIconColor(ContextCompat.getColor(context, R.color.white))
                .setIconSize(50)
                .build()

            val balloon = Balloon.Builder(context)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(text)
                .setTextColorResource(R.color.white)
                .setTextSize(20f)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setIconForm(iconForm)
                .setCornerRadius(8f)
                .setBackgroundColorResource(backgroundColorId)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setTextTypeface(Typeface.SANS_SERIF)
                .setLifecycleOwner(lifecycleOwner)
                .build()
            balloon.showAlignTop(view)
        } catch(E: Exception) {
            Log.d("TAG", "showTooltip ${E.message}")
        }


    }
}