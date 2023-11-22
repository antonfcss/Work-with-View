package com.example.workwithview

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Calendar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CustomClockView(context: Context, attrs: AttributeSet? = null, defaultAttrs: Int = 1) :
    View(context, attrs, defaultAttrs) {
    private var height = 0
    private var width = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    private fun initClock() {
        height = getHeight()
        width = getWidth()
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 16f,
            resources.displayMetrics
        ).toInt()
        val min = min(height, width)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.BLACK)
        drawCircleOutside(canvas)
        drawCircleInside(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawHand(canvas: Canvas, loc: Float, isHour: Boolean, colorHand: Int) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        paint!!.color = colorHand
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint!!
        )
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        hour = if (hour > 12) hour - 12 else hour
        drawHand(
            canvas,
            ((hour * 5f) + (calendar.get(Calendar.MINUTE) * 5f / 60.0)).toFloat(),
            true,
            Color.RED
        )
        drawHand(canvas, calendar.get(Calendar.MINUTE).toFloat(), false, Color.WHITE)
        drawHand(canvas, calendar.get(Calendar.SECOND).toFloat(), false, Color.WHITE)
    }


    private fun drawNumeral(canvas: Canvas) {
        paint!!.textSize = fontSize.toFloat()
        paint!!.color = Color.WHITE
        for (number in numbers) {
            val tmp = number.toRoman()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toInt()
            val y = (height / 2 + sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint!!.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 7f, paint!!)
    }

    private fun drawCircleOutside(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = resources.getColor(R.color.white)
        paint!!.strokeWidth = 5f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), (radius + padding - 10).toFloat(),
            paint!!
        )
    }

    private fun drawCircleInside(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = resources.getColor(R.color.white)
        paint!!.strokeWidth = 1f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), (radius + padding - 70).toFloat(),
            paint!!
        )
    }

    fun Int.toRoman(): String {
        val romanNumbersArray = arrayOfNulls<String>(13)

        romanNumbersArray[0] = ""
        romanNumbersArray[1] = "I"
        romanNumbersArray[2] = "II"
        romanNumbersArray[3] = "III"
        romanNumbersArray[4] = "IV"
        romanNumbersArray[5] = "V"
        romanNumbersArray[6] = "VI"
        romanNumbersArray[7] = "VII"
        romanNumbersArray[8] = "VIII"
        romanNumbersArray[9] = "IX"
        romanNumbersArray[10] = "X"
        romanNumbersArray[11] = "XI"
        romanNumbersArray[12] = "XII"

        return romanNumbersArray[this]!!
    }
}