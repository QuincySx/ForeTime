package com.smallraw.foretime.app.ui.shape

import android.os.Bundle

import com.smallraw.foretime.app.R
import jp.wasabeef.glide.transformations.BlurTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.smallraw.foretime.app.base.BaseActivity
import com.smallraw.foretime.app.databinding.ActivityShapeBinding


class ShapeActivity : BaseActivity() {
    private val mBinding by lazy {
        ActivityShapeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this).load(R.drawable.bg_shape_all)
                .apply(bitmapTransform(BlurTransformation(25, 3))
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(RequestOptions().centerCrop())
                .into(mBinding.ivBackground)
    }
}
