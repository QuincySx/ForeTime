package com.smallraw.foretime.app.ui.shape

import android.os.Bundle

import com.smallraw.foretime.app.R
import jp.wasabeef.glide.transformations.BlurTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.Glide
import com.smallraw.time.base.BaseActivity
import kotlinx.android.synthetic.main.activity_shape.*


class ShapeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shape)

        Glide.with(this).load(R.drawable.bg_shape_all)
                .apply(bitmapTransform(BlurTransformation(25, 3)))
                .into(ivBackground)
    }
}
