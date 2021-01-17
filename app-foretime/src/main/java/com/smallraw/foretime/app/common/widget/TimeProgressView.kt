/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import me.jessyan.autosize.utils.AutoSizeUtils

class TimeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mProgress = 0.0f

    @ColorInt
    private val mProgressLowerColor = Color.parseColor("#D9D9D9")
    private var mProgressLowerHeight: Float = 0.toFloat()

    @ColorInt
    private var mProgressColor = Color.parseColor("#3DCF7A")
    private var mProgressHeight: Float = 0.toFloat()

    private val mProgressLowerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 获取控件默认高度
     *
     * @return 高度
     */
    private val defWidth: Int
        get() = (Math.max(mProgressHeight, mProgressHeight) * 2).toInt()

    init {
        init()
    }

    private fun init() {
        mProgressLowerHeight = AutoSizeUtils.dp2px(context, 3f).toFloat()
        mProgressHeight = mProgressLowerHeight

        mProgressLowerPaint.strokeWidth = mProgressLowerHeight
        mProgressLowerPaint.color = mProgressLowerColor
        mProgressPaint.strokeCap = Paint.Cap.ROUND

        mProgressPaint.strokeWidth = mProgressHeight
        mProgressPaint.color = mProgressColor
        mProgressPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(
            0f,
            mProgressLowerHeight,
            mWidth.toFloat(),
            mProgressLowerHeight,
            mProgressLowerPaint
        )
        canvas.drawLine(0f, mProgressHeight, mWidth * mProgress, mProgressHeight, mProgressPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureWidth(widthMeasureSpec),
            measureHeight(heightMeasureSpec)
        )
    }

    private fun measureHeight(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = defWidth
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = defWidth
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    /**
     * 设置圆圈进度
     *
     * @param progress 0-1
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        postInvalidate()
    }

    /**
     * 设置进度条颜色
     *
     * @param color 颜色
     */
    fun setProgressColor(@ColorInt color: Int) {
        mProgressColor = color
        postInvalidate()
    }
}
