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

    private lateinit var mPorterXfermode: PorterDuffXfermode
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

        invalidateTextPaintAndMeasurements()
        mPorterXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
    }

    private fun invalidateTextPaintAndMeasurements() {

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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
        paint.xfermode = mPorterXfermode

        val rectFOval = when {
            mPhase > 150 -> {
                RectF(
                    rectF.centerX() - radius * (mPhase - 150) / 150,
                    rectF.centerY() - radius,
                    rectF.centerX() + radius * (mPhase - 150) / 150,
                    rectF.centerY() + radius
                )
            }
            mPhase < 150 -> {
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
                canvas.drawArc(rectFCircle, 90f, 180f, false, paint)
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
        canvas.rotate(mRotate.toFloat())
    }


}