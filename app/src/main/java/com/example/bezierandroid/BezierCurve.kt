package com.example.bezierandroid

import kotlin.math.pow

class BezierCurve(
    val sx: Float,
    val sy: Float,
    val cx1: Float,
    val cy1: Float,
    val cx2: Float,
    val cy2: Float,
    val ex: Float,
    val ey: Float,
) {

    val points = mutableListOf<Pair<Double, Double>>()

    fun cubic() {
        var t = 0.0
        while (t <= 1) {
            val curveX = (1 - t).pow(3) * sx + 3 * t * (1 - t).pow(2) * cx1 + 3 * t.pow(2) * (1 - t) * cx2 + t.pow(3) * ex
            val curveY = (1 - t).pow(3) * sy + 3 * t * (1 - t).pow(2) * cy1 + 3 * t.pow(2) * (1 - t) * cy2 + t.pow(3) * ey
            points.add(Pair(curveX, curveY))
            t += 0.0001
        }
    }
}