package com.smallraw.foretime.app.bindingAdapter

import androidx.databinding.BindingAdapter
import com.smallraw.foretime.app.common.widget.TimeProgressView

@BindingAdapter("setProgress")
fun setProgress(view: TimeProgressView, progress: Float) {
    view.setProgress(progress)
}