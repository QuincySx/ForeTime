package com.smallraw.foretime.app.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.databinding.ActivityBaseTitleActivityBinding


abstract class BaseTitleBarActivity : BaseActivity() {
    protected lateinit var mImgTitleBarLeft: ImageView
    protected lateinit var mTvTitleBarContent: TextView
    protected lateinit var mLayoutTitleBarRight: LinearLayout
    protected lateinit var mLayoutTitleBar: ConstraintLayout
    protected lateinit var mLayoutContent: FrameLayout

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_base_title_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databinding = getBinding() as ActivityBaseTitleActivityBinding

        mLayoutTitleBar = databinding.layoutTitleBar
        mLayoutTitleBarRight = databinding.layoutTitleBarRight
        mImgTitleBarLeft = databinding.imgTitleBarLeft
        mTvTitleBarContent = databinding.tvTitleBarContent
        mLayoutContent = databinding.rootContent

        mImgTitleBarLeft.setOnClickListener(selfLeftClickListener())
        mTvTitleBarContent.setText(selfTitleContent())
        mLayoutTitleBar.setBackgroundColor(selfTitleBackgroundColor())
    }

    protected fun addRightView(view: View) {
        mLayoutTitleBarRight.addView(view)
    }

    protected fun addRightView(view: View, layoutParams: ViewGroup.LayoutParams) {
        mLayoutTitleBarRight.addView(view, layoutParams)
    }

    @ColorInt
    protected open fun selfTitleBackgroundColor(): Int {
        return Color.WHITE
    }

    protected open fun selfTitleContent(): String {
        return ""
    }

    protected open fun selfLeftClickListener(): View.OnClickListener? {
        return View.OnClickListener {
            finish()
        }
    }

    override fun setContentView(layoutResID: Int) {
        mLayoutContent.removeAllViews()
        View.inflate(this, layoutResID, mLayoutContent)
        onContentChanged()
    }

    override fun setContentView(view: View) {
        mLayoutContent.removeAllViews()
        mLayoutContent.addView(view)
        onContentChanged()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        mLayoutContent.removeAllViews()
        mLayoutContent.addView(view, params)
        onContentChanged()
    }

    protected fun setTitleBarLeftImage(@DrawableRes res: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, res, null)
        mImgTitleBarLeft.setImageDrawable(drawable)
    }
}