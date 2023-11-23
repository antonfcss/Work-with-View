package com.example.workwithview

import android.graphics.Color

enum class HandType(val color: Int, val thickness: Float) {
    HOUR(Color.RED, 10f),
    MINUTE(Color.WHITE, 4f),
    SECOND(Color.WHITE, 3f)
}