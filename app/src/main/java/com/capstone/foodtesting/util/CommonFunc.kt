package com.capstone.foodtesting.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.MeasureSpec
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.ToastFoodTestingBinding
import com.skydoves.balloon.*
import gun0912.tedimagepicker.util.ToastUtil.context


object CommonFunc {

    fun imageRemoveIPPrefix(url: String?): String {
        return url.toString().removePrefix("https://foodtesting-img.s3.ap-northeast-2.amazonaws.com/img/")
    }

    fun createLottieView(context: Context, assetName: String): LottieAnimationView {
        val lottieView = LottieAnimationView(context)

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )
        lottieView.apply {
            setAnimation(assetName)
            sandboxAnimations()
            layoutParams = lp
        }
        return lottieView

    }

    fun showToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val binding = ToastFoodTestingBinding.inflate(inflater)
        binding.tvToastMessage.text = message
        binding.lvInfo.apply {
            sandboxAnimations()
            playAnimation()
        }
        Toast(context).apply {
            setGravity(Gravity.CENTER or Gravity.BOTTOM, 0, 100.toPx())

            view = binding.root
            duration = Toast.LENGTH_SHORT
        }.show()
    }

    fun showToast(
        context: Context,
        message: String,
        anchorView: View,
        window: Window,
        offsetY: Int = 10,
        offsetX: Int = 0,

    ) {
        val inflater = LayoutInflater.from(context)
        val binding = ToastFoodTestingBinding.inflate(inflater)


        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        // covert anchor view absolute position to a position which is relative to decor view
        // covert anchor view absolute position to a position which is relative to decor view
        val viewLocation = IntArray(2)
        anchorView.getLocationInWindow(viewLocation)
        val viewLeft: Int = viewLocation[0] - rect.left
        val viewTop: Int = viewLocation[1] - rect.top

        // measure toast to center it relatively to the anchor view

        // measure toast to center it relatively to the anchor view
        val metrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics)
        val widthMeasureSpec =
            MeasureSpec.makeMeasureSpec(metrics.widthPixels, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec =
            MeasureSpec.makeMeasureSpec(metrics.heightPixels, MeasureSpec.UNSPECIFIED)



        binding.tvToastMessage.text = message
        binding.lvInfo.apply {
            sandboxAnimations()
            playAnimation()
        }
        Toast(context).apply {
            view = binding.root
            view?.apply {
                measure(widthMeasureSpec, heightMeasureSpec)
                val toastWidth: Int = this.measuredWidth

                // compute toast offsets

                // compute toast offsets
                val toastX: Int = viewLeft + (this.width - toastWidth) / 2 + offsetX
                val toastY: Int = viewTop + this.height + offsetY
                setGravity(Gravity.CENTER or Gravity.BOTTOM, toastX,toastY )
            }
            duration = Toast.LENGTH_SHORT
        }.show()
    }

    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun showTooltip(
        context: Context,
        anchorView: View,
        message: String,
        lifecycleOwner: LifecycleOwner? = null,
        backgroundColorId: Int = R.color.white,
        textColorId: Int = R.color.grey,
        useArrow: Boolean = false,
        arrowOrientation: ArrowOrientation = ArrowOrientation.TOP
    ) {
        try {

            val inflater = LayoutInflater.from(context)
            val binding = ToastFoodTestingBinding.inflate(inflater)


            binding.lvInfo.apply {
                sandboxAnimations()
                playAnimation()

            }
            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColorId))
            binding.tvToastMessage.apply {
                text = message
                setTextColor(ContextCompat.getColor(context, textColorId))
            }

            val balloon = Balloon.Builder(context)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setLayout(binding)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setIsVisibleArrow(useArrow)
                .setCornerRadius(5f)
                .setBackgroundColorResource(backgroundColorId)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setTextTypeface(Typeface.SANS_SERIF)
                .setLifecycleOwner(lifecycleOwner)
//                .setAutoDismissDuration(duration)
                .setArrowOrientation(arrowOrientation)
                .build()

//            val lottieView = balloon.getContentView().findViewById<LottieAnimationView>(R.id.lv_info)



//            val tvMessage = balloon.getContentView().findViewById<TextView>(R.id.tv_toast_message)

            balloon.showAlignTop(anchorView)

        } catch (E: Exception) {
            Log.d("TAG", "showTooltip ${E.message}")

        }


    }
}