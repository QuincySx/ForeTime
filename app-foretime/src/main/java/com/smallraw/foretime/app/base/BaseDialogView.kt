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
package com.smallraw.foretime.app.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.smallraw.foretime.app.R
import me.jessyan.autosize.utils.AutoSizeUtils
import me.jessyan.autosize.utils.ScreenUtils

abstract class BaseDialogView : Dialog {
    private var mWindowManager: WindowManager? = null
    private var mBuilder: Builder? = null

    constructor(builder: Builder) : super(builder.context) {
        mWindowManager = builder.windowManager
        mBuilder = builder
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        init()
    }

    private fun init() {
        if (mWindowManager == null) {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        setContentView(R.layout.base_dialog)
        val patentView = findViewById<LinearLayout>(R.id.patent_view)

        mBuilder?.let {
            patentView.layoutParams =
                FrameLayout.LayoutParams(it.parentWidth, patentView.layoutParams.height)
        }
        val rootView = findViewById<FrameLayout>(R.id.root_view)
        rootView.addView(setRootView())
        initView()
        mBuilder?.apply {
            atView?.let {
                showAtView(rootView, it, atViewPadding, isUp)
            }
        }
    }

    protected abstract fun initView()

    /**
     * 设置显示的主视图
     *
     * @return
     */
    protected abstract fun setRootView(): View

    abstract class Builder {
        var context: Context
        var atView: View? = null
        var atViewPadding: Int = 0
        var parentWidth: Int = 0

        /**
         * 三角显示方向 0：向上 1：向下 2：auto
         */
        var isUp: Boolean = false
        var windowManager: WindowManager? = null

        constructor(context: Context) {
            this.context = context
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            parentWidth = AutoSizeUtils.dp2px(context, 336.toFloat())
        }

        fun setWidth(dp: Float): Builder {
            parentWidth = AutoSizeUtils.dp2px(context, dp)
            return this
        }

        fun atViewDown(view: View): Builder {
            return showAtView(view, 0, false)
        }

        fun atViewDown(view: View, padding: Int): Builder {
            return showAtView(view, padding, false)
        }

        fun atViewUp(view: View, padding: Int): Builder {
            return showAtView(view, padding, true)
        }

        fun atViewUp(view: View): Builder {
            return showAtView(view, 0, true)
        }

        @JvmOverloads
        fun atViewAuto(view: View, padding: Int = 0): Builder {
            val dm = DisplayMetrics()
            windowManager!!.defaultDisplay.getMetrics(dm)
            val screenHeight = dm.heightPixels

            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val viewY = location[1]

            var isUp = false
            if (viewY > screenHeight / 2) {
                isUp = true
            }
            return showAtView(view, padding, isUp)
        }

        fun showAtView(view: View, padding: Int, isUp: Boolean): Builder {
            atView = view
            atViewPadding = padding
            this.isUp = isUp
            return this
        }

        abstract fun build(): com.smallraw.foretime.app.base.BaseDialogView
    }

    private fun showAtView(parentView: ViewGroup, view: View, padding: Int, isUp: Boolean) {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        val viewWidthHalf = view.measuredWidth / 2
        val viewHeight = view.measuredHeight
        window!!.setDimAmount(0.3f)

        val arrow: View
        if (isUp) {
            arrow = findViewById(R.id.doalog_arrow_bottom)
        } else {
            arrow = findViewById(R.id.doalog_arrow_up)
        }

        arrow.visibility = View.VISIBLE

        val layoutParams = arrow.layoutParams

        val marginParams: ViewGroup.MarginLayoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            marginParams = layoutParams
        } else {
            marginParams = ViewGroup.MarginLayoutParams(layoutParams)
        }

        val dm = DisplayMetrics()
        mWindowManager!!.defaultDisplay.getMetrics(dm)
        val screenWidth = dm.widthPixels

        val windowViewX = (screenWidth - mBuilder?.parentWidth!!) / 2

        val arrowWidthHalf = AutoSizeUtils.dp2px(context, 10.toFloat()) / 2

        marginParams.setMargins(viewX - windowViewX + viewWidthHalf - arrowWidthHalf, 0, 0, 0)

        arrow.layoutParams = marginParams

        if (isUp) {
            val screenHeight = dm.heightPixels - viewY
            windowDeploy(0, screenHeight + padding, Gravity.BOTTOM)
        } else {
            val statusBarHeight = ScreenUtils.getStatusBarHeight()
            windowDeploy(0, viewY - statusBarHeight + viewHeight + padding, Gravity.TOP)
        }
    }

    fun windowDeploy(x: Int, y: Int, gravity: Int) {
        val window = window ?: return
//        window.setWindowAnimations(R.style.baseDialogWindowAnim)
        window.setBackgroundDrawableResource(R.color.vifrification)
        val wl = window.attributes
        wl.x = x
        wl.y = y
        wl.gravity = gravity
        window.attributes = wl
    }
}
