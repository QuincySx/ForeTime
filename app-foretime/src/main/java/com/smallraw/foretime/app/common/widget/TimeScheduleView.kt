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
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import me.jessyan.autosize.utils.AutoSizeUtils

class TimeScheduleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mProgress = 0.0f

    private val mPath = Path()
    private val mPathDraw = Path()
    private val mPathMeasure = PathMeasure()

    @ColorInt
    private val mCircleLowerColor = Color.parseColor("#E9E9E9")
    private var mCircleLowerLineWidth: Float = 0.toFloat()
    private var mCircleLowerRadius: Float = 0.toFloat()

    @ColorInt
    private var mProgressColor = Color.parseColor("#5165FF")
    private var mProgressCircleWidth: Float = 0.toFloat()
    private var mProgressCircleRadius: Float = 0.toFloat()

    @ColorInt
    private val mTextColor = Color.parseColor("#333333")
    private var mTextSize: Float = 0.toFloat()
    private var mTextContent = "00:00"
    private val mTextRect = Rect()

    private val mPaintCircleLower = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintText = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 获取控件默认高度
     *
     * @return 高度
     */
    private val defWidth: Int
        get() = (
            Math.max(mProgressCircleRadius, mCircleLowerRadius) * 2 + Math.max(
                mProgressCircleWidth,
                mProgressCircleWidth
            ) * 2
            ).toInt()

    init {
        init()
    }

    private fun init() {
        mCircleLowerLineWidth = AutoSizeUtils.dp2px(context, 2f).toFloat()
        mProgressCircleWidth = AutoSizeUtils.dp2px(context, 7f).toFloat()
        mCircleLowerRadius = AutoSizeUtils.dp2px(context, 85f).toFloat()
        mProgressCircleRadius = mCircleLowerRadius
        mTextSize = AutoSizeUtils.sp2px(context, 36f).toFloat()

        mPaintCircleLower.strokeWidth = mCircleLowerLineWidth
        mPaintCircleLower.color = mCircleLowerColor
        mPaintCircleLower.style = Paint.Style.STROKE

        mPaintCircle.strokeWidth = mProgressCircleWidth
        mPaintCircle.color = mProgressColor
        mPaintCircle.style = Paint.Style.STROKE
        mPaintCircle.strokeCap = Paint.Cap.ROUND

        mPaintText.textSize = mTextSize
        mPaintText.color = mTextColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        mPath.addCircle(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            mProgressCircleRadius,
            Path.Direction.CW
        )
        mPathMeasure.setPath(mPath, false)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            mProgressCircleRadius,
            mPaintCircleLower
        )

        canvas.save()
        canvas.rotate(-90f, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
        mPathDraw.reset()
        mPathMeasure.getSegment(0f, mProgress * mPathMeasure.length, mPathDraw, true)
        canvas.drawPath(mPathDraw, mPaintCircle)
        canvas.restore()

        mPaintText.getTextBounds(mTextContent, 0, mTextContent.length, mTextRect)
        canvas.drawText(
            mTextContent,
            (mWidth / 2 - mTextRect.width() / 2).toFloat(),
            (mWidth / 2 + mTextRect.height() / 2).toFloat(),
            mPaintText
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureWidth(widthMeasureSpec),
            measureHeight(heightMeasureSpec)
        )
    }

    private fun measureHeight(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        var result: Int
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
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        var result: Int
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
     * 设置圆圈颜色
     *
     * @param color 颜色
     */
    fun setProgressColor(@ColorInt color: Int) {
        mProgressColor = color
        mPaintCircle.color = mProgressColor
        postInvalidate()
    }

    /**
     * 设置圆圈中间字体显示
     *
     * @param text 字体内容
     */
    fun setText(text: String) {
        mTextContent = text
        postInvalidate()
    }
}
