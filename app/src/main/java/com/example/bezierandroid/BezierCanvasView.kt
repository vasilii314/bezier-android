package com.example.bezierandroid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.set

private const val STROKE_WIDTH = 20f

class BezierCanvasView(context: Context) : View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.purple_200, null)
    private val auxPointsColor = ResourcesCompat.getColor(resources, R.color.teal_200, null)
    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }
    private val auxPointsPaint = Paint().apply {
        color = auxPointsColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }
    private var pointCounter = 0
    private val pointBuffer = mutableListOf<Pair<Float, Float>>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitmap.isInitialized) {
            extraBitmap.recycle()
        }
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(event)
            MotionEvent.ACTION_MOVE -> touchMove(event)
            MotionEvent.ACTION_UP -> touchUp(event)
        }
        invalidate()
        return true
    }

    private fun touchStart(event: MotionEvent) {
        val motionTouchEventX = event.x
        val motionTouchEventY = event.y
        pointCounter++
        pointBuffer.add(Pair(motionTouchEventX, motionTouchEventY))
        if (pointCounter % 4 == 0) {
            val curve = BezierCurve(
                sx = pointBuffer[0].first,
                sy = pointBuffer[0].second,
                cx1 = pointBuffer[1].first,
                cy1 = pointBuffer[1].second,
                cx2 = pointBuffer[2].first,
                cy2 = pointBuffer[2].second,
                ex = pointBuffer[3].first,
                ey = pointBuffer[3].second
            )
            curve.eval()
            for (point in curve.points) {
                extraCanvas.drawPoint(point.first.toFloat(), point.second.toFloat(), paint)
            }
            val nextCurve = pointBuffer.last()
            pointBuffer.clear()
            pointBuffer.add(nextCurve)
            pointCounter++
        }
        extraCanvas.drawPoint(motionTouchEventX, motionTouchEventY, auxPointsPaint)
    }

    private fun touchMove(event: MotionEvent) {}

    private fun touchUp(event: MotionEvent) {}
}