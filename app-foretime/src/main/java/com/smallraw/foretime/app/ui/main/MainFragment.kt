package com.smallraw.foretime.app.ui.main

import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DrawableRes
import android.util.Log
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.config.getCalendarSettingConfig
import com.smallraw.foretime.app.config.getDefCalendarSettingConfig
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.ui.main.calendar.CalendarFragment
import com.smallraw.foretime.app.ui.main.tomatoBell.TomatoBellFragment
import com.smallraw.library.core.extensions.expandTouchArea
import com.smallraw.time.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment(), OnMainFragmentCallback {
    private lateinit var viewPagerAdapter: FragmentStateAdapter
    private val mTotalCount = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mTotalCount
            }

            override fun createFragment(position: Int): Fragment {
                when (position) {
                    0 -> TomatoBellFragment.newInstance(this@MainFragment)
                    1 -> CalendarFragment.newInstance(this@MainFragment)
                }
                throw RuntimeException("Fragment State Adapter TotalCount error")
            }
        }

        initView()

        App.getInstance().getAppExecutors().diskIO().execute {

            getDefCalendarSettingConfig().saveConfig()
            Log.e("== read ==", getDefCalendarSettingConfig().toString())
            val taskSettingConfig = getCalendarSettingConfig()
            Log.e("==sss==", taskSettingConfig.toString())
        }
//        CalendarConfigInfo().saveConfig()
//        Log.e("== read ==", DefConfig.mCalendarSettingConfig.get().toString())
    }


    private fun initView() {
        viewPager.adapter = viewPagerAdapter
        ivTomatoBell.isChecked = true

        ivCalendar.expandTouchArea(100)

//        tomatoBellFragment.showViewAction()

//        ivSuspension.setOnDragListener { v, event ->
//            when (event.action) {
//                DragEvent.ACTION_DRAG_ENTERED -> {
//                    v.setBackgroundColor(GREEN)
//                }
//                DragEvent.ACTION_DRAG_EXITED -> {
//                    v.setBackgroundColor(RED)
//                }
//                DragEvent.ACTION_DRAG_ENDED -> {
//                    v.setBackgroundColor(WHITE)
//                }
//                DragEvent.ACTION_DROP -> {
//                    Toast.makeText(applicationContext, "tuodong", Toast.LENGTH_LONG).show()
////                    val dropX = event.x
////                    val dropY = event.y
////                    val state = event.localState as DragData
////
////                    val shape = LayoutInflater.from(this).inflate(
////                            R.layout.view_shape, dropContainer, false) as ImageView
////                    shape.setImageResource(state.item.getImageDrawable())
////                    shape.setX(dropX - state.width.toFloat() / 2)
////                    shape.setY(dropY - state.height.toFloat() / 2)
////                    shape.getLayoutParams().width = state.width
////                    shape.getLayoutParams().height = state.height
////                    dropContainer.addView(shape)
//                }
//                else -> {
//                }
//            }
//            true
//        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.ivTomatoBell -> {
                viewPager.currentItem = 0
                ivTomatoBell.isChecked = true
                ivCalendar.isChecked = false
                ivCalendar.expandTouchArea(100)
//                calendarFragment.hiddenViewAction()
//                tomatoBellFragment.showViewAction()
            }
            R.id.ivCalendar -> {
                viewPager.currentItem = 1
                ivTomatoBell.isChecked = false
                ivCalendar.isChecked = true
                ivTomatoBell.expandTouchArea(100)
//                tomatoBellFragment.hiddenViewAction()
//                calendarFragment.showViewAction()
            }
        }
    }

    private fun setTouchDelegate(view: View, expandTouchWidth: Int) {
        val parentView = view.parent as ViewGroup
        view.post {
            val rect = Rect()
            view.getHitRect(rect)
            rect.top -= expandTouchWidth
            rect.bottom += expandTouchWidth
            rect.left -= expandTouchWidth
            rect.right += expandTouchWidth
            parentView.touchDelegate = TouchDelegate(rect, view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onChangeIvSuspension(@DrawableRes id: Int) {
        if (ivSuspension != null) {
            ivSuspension.setBackgroundResource(id)
        }
    }

    override fun setOnTouchEventListener(onTouchListener: View.OnTouchListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnTouchListener(onTouchListener)
        }
    }

    override fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnLongClickListener(onLongClickListener)
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnClickListener(onClickListener)
        }
    }
}
