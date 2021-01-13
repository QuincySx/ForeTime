package com.smallraw.foretime.app.bindingAdapter

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.smallraw.foretime.app.common.widget.TimeScheduleView

@BindingAdapter("setProgress")
fun setProgress(view: TimeScheduleView, progress: Float) {
    view.setProgress(progress)
}

@BindingAdapter("setTimeWrapper")
fun setTimeWrapper(view: TimeScheduleView, timeWrapper: String) {
    view.setText(timeWrapper)
}

@BindingAdapter("setProgressColor")
fun setProgressColor(view: TimeScheduleView, @ColorInt progress: Int) {
    view.setProgressColor(progress)
}
