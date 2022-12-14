package com.capstone.foodtesting.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.DragEvent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.capstone.foodtesting.R

class GradientStrokeCardView
    (
    context: Context,
    attributeSet: AttributeSet?
) : CardView(context, attributeSet) {


    private val StrokeWidth: Float

    private val GradientColors: IntArray

    private val GradientAnimate: Boolean

    private val AnimationDuration: Int

    private val mHandler: Handler

    private var sX = 0f
    private var sY = 0f
    private var eX = 0f
    private var eY = 0f

    private var t: Int = 0

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.GradientStrokeCardView, 0, 0)
            .apply {
                try {
                    val resourceId =
                        getResourceId(R.styleable.GradientStrokeCardView_GSC_GradientColors, 0)

                    GradientColors = if (resourceId == 0) {
                        IntArray(2) { ContextCompat.getColor(context, android.R.color.transparent) }

                    } else {
                        val colorsIds = context.resources.getStringArray(resourceId)

                        val colors = mutableListOf<Int>()

                        colorsIds.forEach {
                            colors.add(Color.parseColor(it))
                        }

                        colors.toIntArray()
                    }
                    StrokeWidth =
                        getDimension(
                            R.styleable.GradientStrokeCardView_GSC_StrokeWidth,
                            2f.dpToPx()
                        )

                    GradientAnimate =
                        getBoolean(R.styleable.GradientStrokeCardView_GSC_GradientAnimate, false)

                    AnimationDuration =
                        getInteger(R.styleable.GradientStrokeCardView_GSC_AnimationDuration, 10)

                    mHandler = Handler(Looper.getMainLooper())
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (GradientAnimate) {
            mHandler.postDelayed({

                t += 1
                if (t >= 360) {
                    t -= 360
                }
                val angle = Math.PI / 180 * t

                calculateCoordinate(angle, width.toFloat(), height.toFloat())

                invalidate()
            }, 0.001.toLong())
        } else {
            sX = 0f
            sY = 0f
            eX = width.toFloat()
            eY = 0f
        }

        drawStroke(canvas, sX, sY, eX, eY)
    }


    private fun drawStroke(canvas: Canvas, sX: Float, sY: Float, eX: Float, eY: Float) {
        val paint = Paint()

        paint.apply {
            this.strokeWidth = StrokeWidth
            this.strokeCap = Paint.Cap.ROUND //?????? ???????????? ?????? ??????
            this.strokeJoin = Paint.Join.ROUND //????????? ??????
            this.style = Paint.Style.STROKE
            this.isAntiAlias = true
            this.shader = LinearGradient(
                sX, //??????????????? ????????? ?????? x??????
                sY, //??????????????? ????????? ?????? y??????
                eX, //??????????????? ????????? ??? x??????
                eY, //??????????????? ????????? ??? y??????
                GradientColors,
                null,
                Shader.TileMode.MIRROR
            )

        }

        val rect = RectF(
            StrokeWidth / 2,
            StrokeWidth / 2,
            width.toFloat() - StrokeWidth / 2,
            height.toFloat() - StrokeWidth / 2
        )

        //???????????? ?????? Radius ?????? StrokeWidth ??? ????????????
        canvas.drawRoundRect(
            rect,
            radius - StrokeWidth.pxToDp(),
            radius - StrokeWidth.pxToDp(),
            paint
        )

    }


    private fun calculateCoordinate(angle: Double, width: Float, height: Float) {
        val w2h2 = Math.pow(width / 2.toDouble(), 2.toDouble()) + Math.pow(
            height / 2.toDouble(),
            2.toDouble()
        )
        val r = Math.sqrt(w2h2).toFloat()
        val w = r * Math.cos(angle)
        val h = r * Math.sin(angle)


        // ?????? width/2 ??? height/2 ????????? ?????? ??????????????? ?????????
        // ?????? w.toFloat()??? h.toFloat()??? ????????? ?????? ????????? (0,0)????????? ???????????? ?????????
        // ????????? ????????? ????????? (wid/2,hei/2)??? ????????????????????????
        sX = -w.toFloat() + width / 2
        sY = -h.toFloat() + height / 2
        eX = w.toFloat() + width / 2
        eY = h.toFloat() + height / 2
    }

    //dp??? px??? ?????? (dp??? ???????????? px??? ??????)
    private fun Float.dpToPx(): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    //px??? dp??? ?????? (px??? ???????????? dp??? ??????)
    private fun Float.pxToDp(): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return this / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}