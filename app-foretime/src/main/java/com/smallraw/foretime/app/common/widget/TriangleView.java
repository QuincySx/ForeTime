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
package com.smallraw.foretime.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import com.smallraw.foretime.app.R;

public class TriangleView extends View {
    private float mHeight;
    private float mWidth;
    int mTriangleDirection = 0; // 0 top 1 bottom

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray =
                context.getTheme()
                        .obtainStyledAttributes(attrs, R.styleable.TriangleView, defStyleAttr, 0);
        mTriangleDirection = typedArray.getInt(R.styleable.TriangleView_triangleDirection, 0);
        typedArray.recycle();
        init();
    }

    private void init() {
        mHeight = AutoSizeUtils.dp2px(getContext(), 12);
        mWidth = AutoSizeUtils.dp2px(getContext(), 10);

        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStyle(Paint.Style.FILL);

        if (mTriangleDirection == 0) {
            mPath.moveTo(0, mHeight);
            mPath.lineTo(mWidth, mHeight);
            mPath.lineTo(mWidth / 2, 0);
            mPath.close();
        } else {
            mPath.moveTo(0, 0);
            mPath.lineTo(mWidth, 0);
            mPath.lineTo(mWidth / 2, mHeight);
            mPath.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getDefHeight();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getDefWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int getDefHeight() {
        return (int) mHeight;
    }

    private int getDefWidth() {
        return (int) mWidth;
    }
}
