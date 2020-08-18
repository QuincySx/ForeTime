package com.smallraw.foretime.app.ui.main

import androidx.annotation.DrawableRes
import android.view.View

interface OnMainFragmentCallback {

    fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?)

    fun setOnTouchEventListener(onTouchListener: View.OnTouchListener?)

    fun setOnClickListener(onClickListener: View.OnClickListener?)
}