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
            this.strokeCap = Paint.Cap.ROUND //둥근 모양으로 끝이 장식
            this.strokeJoin = Paint.Join.ROUND //모서리 처리
            this.style = Paint.Style.STROKE
            this.isAntiAlias = true
            this.shader = LinearGradient(
                sX, //그라디언트 라인의 시작 x좌표
                sY, //그라디언트 라인의 시작 y좌표
                eX, //그라디언트 라인의 끝 x좌표
                eY, //그라디언트 라인의 끝 y좌표
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

        //카드뷰의 안쪽 Radius 라서 StrokeWidth 를 빼줘야함
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


        // 밑에 width/2 랑 height/2 더한건 좌표 옮겨준거랑 똑같음
        // 그냥 w.toFloat()랑 h.toFloat()로 쓰는건 중심 좌표가 (0,0)일때를 가정하고 한건데
        // 여기서 필요한 좌표는 (wid/2,hei/2)가 중심좌표일때라서
        sX = -w.toFloat() + width / 2
        sY = -h.toFloat() + height / 2
        eX = w.toFloat() + width / 2
        eY = h.toFloat() + height / 2
    }

    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
    private fun Float.dpToPx(): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
    private fun Float.pxToDp(): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return this / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}