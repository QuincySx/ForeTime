package com.smallraw.foretime.app.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

@BindingAdapter("android:src")
fun setImageSrcCompatResource(imageView: ImageView, resource: Int) {
    if (resource == 0) {
        return
    }

    imageView.background = VectorDrawableCompat.create(imageView.resources,resource, imageView.context.theme)
}