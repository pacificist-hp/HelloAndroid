package com.android.pacificist.helloandroid.ui.interpolator

import android.graphics.PointF
import android.view.animation.Interpolator

class EaseCubicInterpolator(x1: Float, y1: Float, x2: Float, y2: Float) : Interpolator {

    private val accuracy = 4096
    private val control1 = PointF(x1, y1)
    private val control2 = PointF(x2, y2)

    private var lastI = 0

    override fun getInterpolation(input: Float): Float {
        var t = input
        for (i in lastI until accuracy) {
            t = 1.0f * i / accuracy
            val x = cubicCurves(t, 0f, control1.x, control2.x, 1f)
            if (x >= input) {
                lastI = i
                break
            }
        }

        var value = cubicCurves(t, 0f, control1.y, control2.y, 1f)
        if (value > 0.999) {
            value = 1f
            lastI = 0
        }

        return value
    }

    private fun cubicCurves(
        t: Float,
        value0: Float,
        value1: Float,
        value2: Float,
        value3: Float
    ): Float {
        val u = 1 - t
        val tt = t * t
        val uu = u * u
        val uuu = uu * u
        val ttt = tt * t

        var value = uuu * value0
        value += 3 * uu * t * value1
        value += 3 * u * tt * value2
        value += ttt * value3

        return value
    }
}