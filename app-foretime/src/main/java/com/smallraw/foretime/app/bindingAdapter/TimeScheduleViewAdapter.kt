package com.smallraw.foretime.app.bindingAdapter

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.smallraw.foretime.app.common.widget.TimeScheduleView

object TimeScheduleViewAdapter {
    @BindingAdapter("app:setProgress")
    fun setProgress(view: TimeScheduleView, progress: Float) {
        view.setProgress(progress)
    }

    @BindingAdapter("app:setTimeWrapper")
    fun setTimeWrapper(view: TimeScheduleView, timeWrapper: String) {
        view.setText(timeWrapper)
    }

    @BindingAdapter("app:setProgressColor")
    fun setProgressColor(view: TimeScheduleView, @ColorInt progress: Int) {
        view.setProgressColor(progress)
    }
}
