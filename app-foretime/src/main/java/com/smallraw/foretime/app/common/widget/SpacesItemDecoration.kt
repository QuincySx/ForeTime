package com.smallraw.foretime.app.common.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(private val space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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
