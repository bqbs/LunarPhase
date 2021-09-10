package com.github.bqbs.lunarphase

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min

/**
 * TODO: document your custom view class.
 */
class LunarPhaseView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr) {

    var mRotate: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    var mPhase: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint by lazy { Paint() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(context, attrs, defStyleAttr, 0)

    init {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.LunarPhaseView, defStyleAttr, 0
        )

        a.recycle()

    }

    @SuppressLint("DrawAllocation")
     override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 黑色背景上面画了个橙色的正圆
        canvas.drawColor(ContextCompat.getColor(context!!, android.R.color.black))
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        paint.color = ContextCompat.getColor(context!!, android.R.color.holo_orange_light)
        val radius = min(width, height) * 0.3f
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, paint)

        val c = canvas.saveLayer(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        paint.isDither = true
        paint.xfermode =  PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

        // 下面的这些计算跟mPhase的改变方式有关
        // 首先mPhase 是由CountDownTimer进行修改的 
        // 创建一个矩形，固定中心在屏幕中间
        val rectFOval = when {
            mPhase > 150 -> {
                // 这里椭圆的 `Minor axis` 在变小   眉月 -> 上弦月
                RectF(
                    rectF.centerX() - radius * (mPhase - 150) / 150,
                    rectF.centerY() - radius,
                    rectF.centerX() + radius * (mPhase - 150) / 150,
                    rectF.centerY() + radius
                )
            }
            mPhase < 150 -> {
                // 这里椭圆的 `Minor axis` 在变大。上弦月 -> 盈凸月
                RectF(
                    rectF.centerX() - (radius - radius * mPhase / 150),
                    rectF.centerY() - radius,
                    rectF.centerX() + (radius - radius * mPhase / 150),
                    rectF.centerY() + radius
                )
            }
            else -> {
                null
            }
        }
        
        val rectFCircle = RectF(
            rectF.centerX() - radius,
            rectF.centerY() - radius,
            rectF.centerX() + radius,
            rectF.centerY() + radius

        )

        paint.color = ContextCompat.getColor(context!!, android.R.color.black)

        when {
            mPhase == 150 -> {
                if (rectFOval != null) {
                    canvas.drawOval(rectFOval, paint)
                }
                canvas.drawArc(rectFCircle, 90f, 180f, false, paint)
            }
            mPhase == 0 -> {
            }
            mPhase < 150 -> {
                // 先画半圆，再画椭圆
                canvas.drawArc(rectFCircle, 90f, 180f, false, paint)
                // 当 'Minor axis' 的长度减少 0, 然后再增加。月相的变化是 眉月 -> 上弦月 -> 盈凸月
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                canvas.drawOval(rectFOval!!, paint)
            }
            else -> {
                canvas.drawOval(rectFOval!!, paint)
                canvas.drawArc(rectFCircle, 90f, 180f, false, paint)
            }
        }
        paint.xfermode = null
        canvas.restoreToCount(c)
        Log.d("LunarPhase", "mRotate=$mRotate")
        // Rotate the canvas. For recording preview.
        canvas.rotate(mRotate.toFloat())
    }


}