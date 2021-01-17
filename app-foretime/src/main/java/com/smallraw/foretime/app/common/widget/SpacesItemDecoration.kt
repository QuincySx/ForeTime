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
package com.smallraw.foretime.app.common.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: androidx.recyclerview.widget.RecyclerView,
        state: androidx.recyclerview.widget.RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        //    outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        //    if (parent.getChildPosition(view) == 0)
        //      outRect.top = space;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0
        }
        if (parent.getChildAdapterPosition(view) == if (parent.adapter == null) 0 else parent.adapter!!.itemCount - 1) {
            outRect.right = 0
        }
    }
}
