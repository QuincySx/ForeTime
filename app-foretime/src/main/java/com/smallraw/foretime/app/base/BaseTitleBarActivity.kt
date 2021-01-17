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

import android.annotation.SuppressLint
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
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.smallraw.foretime.app.R

abstract class BaseTitleBarActivity : BaseActivity() {
    protected lateinit var mImgTitleBarLeft: ImageView
    protected lateinit var mTvTitleBarContent: TextView
    protected lateinit var mLayoutTitleBarRight: LinearLayout
    protected lateinit var mLayoutTitleBar: ConstraintLayout
    protected lateinit var mLayoutContent: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImgTitleBarLeft = findViewById(R.id.img_title_bar_left)
        mTvTitleBarContent = findViewById(R.id.tv_title_bar_content)
        mLayoutTitleBarRight = findViewById(R.id.layout_title_bar_right)
        mLayoutTitleBar = findViewById(R.id.layout_title_bar)
        mLayoutContent = findViewById(R.id.root_content)

        mImgTitleBarLeft.setOnClickListener(selfLeftClickListener())
        mTvTitleBarContent.text = selfTitleContent()
        mLayoutTitleBar.setBackgroundColor(selfTitleBackgroundColor())
    }

    @SuppressLint("WrongViewCast")
    override fun getDataBindingContentView(): ViewGroup {
        return findViewById(R.id.root_content)
    }

    @LayoutRes
    override fun getDataBindingRootViewId(): Int {
        return R.layout.activity_base_title_activity
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

    protected fun setTitleBarLeftImage(@DrawableRes res: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, res, null)
        mImgTitleBarLeft.setImageDrawable(drawable)
    }
}
