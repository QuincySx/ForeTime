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
package com.smallraw.foretime.app.ui.decoration;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    /**
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies the
     * number of pixels that the item view should be inset by, similar to padding or margin. The
     * default implementation sets the bounds of outRect to 0 and returns.
     *
     * <p>
     *
     * <p>If this ItemDecoration does not affect the positioning of item views, it should set all
     * four fields of <code>outRect</code> (left, top, right, bottom) to zero before returning.
     *
     * <p>
     *
     * <p>If you need to access Adapter for additional data, you can call {@link
     * RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the View.
     *
     * @param outRect Rect to receive the output.
     * @param view The child view to decorate
     * @param parent RecyclerView this ItemDecoration is decorating
     * @param state The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = mSpace;
        }
    }

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }
}
