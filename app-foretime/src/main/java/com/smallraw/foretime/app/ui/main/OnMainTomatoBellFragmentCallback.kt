package com.smallraw.foretime.app.ui.main

import android.view.View

interface OnMainTomatoBellFragmentCallback {

    fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?)

    fun setOnTouchEventListener(onTouchListener: View.OnTouchListener?)

    fun setOnClickListener(onClickListener: View.OnClickListener?)
}