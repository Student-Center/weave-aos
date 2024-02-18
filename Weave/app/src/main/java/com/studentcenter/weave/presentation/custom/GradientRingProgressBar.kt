package com.studentcenter.weave.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class GradientRingProgressBar : View {

    private val progressPaint: Paint = Paint()
    private val backgroundPaint: Paint = Paint()
    private val progressRectF: RectF = RectF()

    private var progress = 80
    private lateinit var gradient: LinearGradient

    private val colorArray = intArrayOf(
        Color.parseColor("#CC3D98"),
        Color.parseColor("#CC448D"),
        Color.parseColor("#CC596E"),
        Color.parseColor("#CE7853"),
        Color.parseColor("#D0923C"),
        Color.parseColor("#BEA323"),
        Color.parseColor("#B0B10F"),
        Color.parseColor("#9BBB11")
    )

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        progressPaint.isAntiAlias = true
        progressPaint.strokeWidth = 30f
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND

        backgroundPaint.isAntiAlias = true
        backgroundPaint.strokeWidth = 30f
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.color = Color.GRAY
        backgroundPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (min(width, height) / 2f) * 0.8f

        progressRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        val anglePerColor = 330f / colorArray.size
        val positions = FloatArray(colorArray.size)

        for (i in colorArray.indices) {
            positions[i] = i * anglePerColor / 330f
        }

        gradient = LinearGradient(
            centerX - radius, centerY,
            centerX + radius, centerY,
            colorArray,
            positions,
            Shader.TileMode.MIRROR
        )

        progressPaint.shader = gradient

        canvas.drawArc(progressRectF, -90f, 360f * progress / 100f, false, backgroundPaint)
        canvas.drawArc(progressRectF, -90f, 360f * progress / 100f, false, progressPaint)
    }
}
