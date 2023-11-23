package com.example.workwithview

import android.graphics.Color

enum class HandType(val color: Int, val thickness: Float, val length: Float) {
    HOUR(Color.RED, 9f, 0.5f),
    MINUTE(Color.WHITE, 5f, 0.7f),
    SECOND(Color.WHITE, 3f, 0.8f)
}