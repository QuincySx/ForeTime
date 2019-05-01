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
import com.smallraw.foretime.app.R
import me.jessyan.autosize.utils.ScreenUtils

abstract class BaseDialogView : Dialog {
    private var mWindowManager: WindowManager? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        init()
    }

    private fun init() {
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        setContentView(R.layout.base_dialog)
        val rootView = findViewById<FrameLayout>(R.id.root_view)
        rootView.addView(setRootView())
        initView()
    }

    protected abstract fun initView()

    /**
     * 设置显示的主视图
     *
     * @return
     */
    protected abstract fun setRootView(): View

    fun showAtViewDown(view: View) {
        showAtView(view, 0, false)
    }

    fun showAtViewDown(view: View, padding: Int) {
        showAtView(view, padding, false)
    }

    fun showAtViewUp(view: View, padding: Int) {
        showAtView(view, padding, true)
    }

    fun showAtViewUp(view: View) {
        showAtView(view, 0, true)
    }

    @JvmOverloads
    fun showAtViewAuto(view: View, padding: Int = 0) {
        val dm = DisplayMetrics()
        mWindowManager!!.defaultDisplay.getMetrics(dm)
        val screenHeight = dm.heightPixels

        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewY = location[1]

        var isUp = false
        if (viewY > screenHeight / 2) {
            isUp = true
        }
        showAtView(view, padding, isUp)
    }

    fun showAtView(view: View, padding: Int, isUp: Boolean) {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        val viewWidthHalf = view.measuredWidth / 2
        val viewHeight = view.measuredHeight
        window!!.setDimAmount(0.3f)
        show()

        val parentView = findViewById<ViewGroup>(R.id.patent_view)
        val arrow: View
        if (isUp) {
            arrow = parentView.findViewById(R.id.doalog_arrow_bottom)
        } else {
            arrow = parentView.findViewById(R.id.doalog_arrow_up)
        }

        arrow.visibility = View.VISIBLE
        arrow.post {
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

            val windowViewX = (screenWidth - parentView.measuredWidth) / 2

            val arrowWidthHalf = arrow.width / 2

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
    }

    fun windowDeploy(x: Int, y: Int, gravity: Int) {
        val window = window ?: return
        window.setWindowAnimations(R.style.baseDialogWindowAnim)
        window.setBackgroundDrawableResource(R.color.vifrification)
        val wl = window.attributes
        wl.x = x
        wl.y = y
        wl.gravity = gravity
        window.attributes = wl
    }


}
