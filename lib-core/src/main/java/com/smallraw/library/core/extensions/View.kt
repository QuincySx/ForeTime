package com.smallraw.library.core.extensions

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

fun View.expandTouchArea(size: Int = 10) {
    val parentView: View = this.parent as View
    parentView.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= size
        rect.bottom += size
        rect.left -= size
        rect.right += size
        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}