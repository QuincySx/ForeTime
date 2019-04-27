package com.smallraw.foretime.app.common.widget

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener

class DragRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        this.setOnDragListener(mDragListener)
    }

    private var dragedView: View? = null//被拖拽的视图

    private val mDragListener = OnDragListener { v, event ->
        /**
         * ACTION_DRAG_STARTED:当拖拽操作执行时，就会执行一次
         * DragEvent.ACTION_DRAG_ENDED：当拖拽事件结束，手指抬起时，就是执行一次
         * DragEvent.ACTION_DRAG_ENTERED：当手指进入设置了拖拽监听的控件范围内的瞬间执行一次
         * DragEvent.ACTION_DRAG_EXITED：当手指离开设置了拖拽监听的控件范围内的瞬间执行一次
         * DragEvent.ACTION_DRAG_LOCATION：当手指在设置了拖拽监听的控件范围内，移动时，实时会执行，执行N次
         * DragEvent.ACTION_DROP：当手指在设置了拖拽监听的控件范围内松开时，执行一次
         *
         * @param v 当前监听拖拽事件的view(其实就是mGridLayout)
         * @param event 拖拽事件
         * @return
         */
        when (event.action) {
            //当拖拽事件开始时，创建出与子控件对应的矩形数组
            DragEvent.ACTION_DRAG_STARTED -> initRects()
            DragEvent.ACTION_DRAG_LOCATION -> {
                //手指移动时，实时判断触摸是否进入了某一个子控件
                val touchIndex = getTouchIndex(event)
                //说明触摸点进入了某一个子控件,判断被拖拽的视图与进入的子控件对象不是同一个的时候才进行删除添加操作

                if (touchIndex > -1 && dragedView != null && dragedView !== this@DragRecyclerView.getChildAt(touchIndex)) {
                    this@DragRecyclerView.removeView(dragedView)
                    this@DragRecyclerView.addView(dragedView, touchIndex)
                }
            }
            DragEvent.ACTION_DRAG_ENDED ->
                //拖拽事件结束后，让被拖拽的view设置为可用，否则背景变红，并且长按事件会失效
                if (dragedView != null) {
                    dragedView!!.setEnabled(true)
                }
        }

        true
    }

//    private val mLongClickListener = OnLongClickListener { v ->
//        //长按时，开始拖拽操作，显示出阴影
//        //被拖拽的视图其实就是v参数
//        dragedView = v
//        ViewCompat.startDragAndDrop(v, null, View.DragShadowBuilder(v), null, 0)
//        v.isEnabled = false
//        //v.startDragAndDrop(null, new View.DragShadowBuilder(v), null, 0); // api24
//        true
//    }

    //手指移动时，实时判断触摸是否进入了某一个子控件
    private fun getTouchIndex(event: DragEvent): Int {
        //遍历所有的数组，如果包含了当前的触摸点返回索引即可
        if (null == mRects) {
            return -1
        }
        for (i in 0..mRects!!.size) {
            val rect = mRects!![i]
            if (rect.contains(event.x.toInt(), event.y.toInt())) {
                return i
            }
        }
        return -1
    }


    //当拖拽事件开始时，创建出与子控件对应的矩形数组
    private var mRects: Array<Rect>? = null

    private fun initRects() {
        mRects = Array(this.childCount, { Rect() })

        for (i in 0 until this.childCount) {
            val childView = this.getChildAt(i)
            //创建与每个子控件对应矩形对象
            val rect = Rect(childView.left, childView.top, childView.right, childView.bottom)
            mRects!![i] = rect
        }
    }
}
