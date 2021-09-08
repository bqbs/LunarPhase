package com.jclian.moonphase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * TODO: document your custom view class.
 */
class MoonPhaseView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr) {

    private lateinit var mPorterXfermode: PorterDuffXfermode


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
            attrs, R.styleable.MoonPhaseView, defStyleAttr, 0
        )

        a.recycle()

        invalidateTextPaintAndMeasurements()
        mPorterXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private fun invalidateTextPaintAndMeasurements() {

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(ContextCompat.getColor(context!!, android.R.color.black))
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val c = canvas.saveLayer(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        val radius = width * 0.3f
        paint.isDither = true
        paint.color = ContextCompat.getColor(context!!, android.R.color.holo_orange_light)
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, paint)
        paint.xfermode = mPorterXfermode

        val rectFOval = if (mPhase > 15) {
            RectF(
                rectF.centerX() - radius * mPhase / 30,
                rectF.centerY() - radius,
                rectF.centerX() + radius * mPhase / 30,
                rectF.centerY() + radius
            )
        } else {
            RectF(
                rectF.centerX() + radius * mPhase / 30,
                rectF.centerY() - radius,
                rectF.centerX() - radius * mPhase / 30,
                rectF.centerY() + radius
            )
        }
        val rectFCircle = RectF(
            rectF.centerX() - radius,
            rectF.centerY() - radius,
            rectF.centerX() + radius,
            rectF.centerY() + radius

        )

        paint.color = ContextCompat.getColor(context!!, android.R.color.black)

        if (mPhase == 15) {
        } else if (mPhase < 15) {
            canvas.drawOval(rectFOval, paint)
            canvas.drawArc(rectFCircle, -90f, 180f, false, paint)
        } else {
            canvas.drawOval(rectFOval, paint)
            canvas.drawArc(rectFCircle, 90f, 180f, false, paint)
        }
        paint.xfermode = null
        canvas.restoreToCount(c)
    }


    var mPhase: Int = 0
        set(value) {
            field = value
            invalidate()
        }
}