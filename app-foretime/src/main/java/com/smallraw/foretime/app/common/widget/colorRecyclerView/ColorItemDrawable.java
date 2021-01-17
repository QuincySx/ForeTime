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
package com.smallraw.foretime.app.common.widget.colorRecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;

public class ColorItemDrawable extends StateListDrawable {
    private ColorItemNormalDrawable mColorItemNormalDrawable;
    private ColorItemSelectDrawable mColorItemSelectDrawable;
    private int mColor = Color.parseColor("#139EED");

    public ColorItemDrawable(Context context) {
        mColorItemNormalDrawable = new ColorItemNormalDrawable(context);
        mColorItemSelectDrawable = new ColorItemSelectDrawable(context);

        addState(new int[] {android.R.attr.state_pressed}, mColorItemSelectDrawable);
        addState(new int[] {android.R.attr.state_selected}, mColorItemSelectDrawable);

        addState(new int[] {}, mColorItemNormalDrawable);
    }

    public void setColor(int color) {
        mColor = color;
        mColorItemNormalDrawable.setColor(mColor);
        mColorItemSelectDrawable.setColor(mColor);
        invalidateSelf();
    }
}
