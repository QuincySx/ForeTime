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
package com.smallraw.foretime.app.ui.shape

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.databinding.ActivityShapeBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class ShapeActivity : BaseActivity() {
    private val mBinding by lazy {
        getBinding() as ActivityShapeBinding
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_shape)
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this).load(R.drawable.bg_shape_all)
            .apply(
                bitmapTransform(BlurTransformation(25, 3))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            )
            .apply(RequestOptions().centerCrop())
            .into(mBinding.ivBackground)
    }
}
