package com.smallraw.foretime.app.ui.main

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseFragment
import com.smallraw.foretime.app.config.getCalendarSettingConfig
import com.smallraw.foretime.app.config.getDefCalendarSettingConfig
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.ui.main.calendar.CalendarFragment
import com.smallraw.foretime.app.ui.main.tomatoBell.TomatoBellFragment
import com.smallraw.library.core.extensions.awaitEnd
import com.smallraw.library.core.extensions.expandTouchArea
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.launch


class MainFragment : BaseFragment(), View.OnClickListener {
    private lateinit var viewPagerAdapter: FragmentStateAdapter
    private val mTotalCount = 2
    private val mMainScreenViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainScreenViewModel::class.java)
    }

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
                return when (position) {
                    MainPageIndex.TOMATO_BELL -> TomatoBellFragment.newInstance(
                        mOnMainTomatoBellFragmentCallback
                    )
                    MainPageIndex.CALENDAR -> CalendarFragment.newInstance(
                        mOnMainCalendarFragmentCallback
                    )
                    else -> throw RuntimeException("Fragment State Adapter TotalCount error")
                }
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


    private val mCalendarSuspensionAnim by lazy {
        val translate = ValueAnimator.ofFloat(0f, 1f)
        translate.addUpdateListener { animation ->
            val scale = animation.animatedValue.toString().toFloat()
            ivCalendarSuspension.setScaleX(scale)
            ivCalendarSuspension.setScaleY(scale)
        }
        translate.duration = 140
        translate
    }

    private val mTomatoBellSuspensionAnim by lazy {
        val translate = ValueAnimator.ofFloat(0f, 1f)
        translate.addUpdateListener { animation ->
            val scale = animation.animatedValue.toString().toFloat()
            ivTomatoBellSuspension.setScaleX(scale)
            ivTomatoBellSuspension.setScaleY(scale)
        }
        translate.duration = 140
        translate
    }

    private fun initView() {
        viewPager.setUserInputEnabled(false)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2

        ivTomatoBell.isChecked = true

        ivTomatoBell.setOnClickListener(this)
        ivCalendar.setOnClickListener(this)

        ivCalendar.expandTouchArea(100)

        mMainScreenViewModel.mMainPageIndex.observe(viewLifecycleOwner) {
            if (viewPager.currentItem == it) {
                return@observe
            }
            viewPager.currentItem = it
            when (it) {
                MainPageIndex.TOMATO_BELL -> {
                    lifecycleScope.launch {
                        ivTomatoBell.isChecked = true
                        ivCalendar.isChecked = false
                        mCalendarSuspensionAnim.cancel()
                        mTomatoBellSuspensionAnim.cancel()
                        mCalendarSuspensionAnim.reverse()
                        mCalendarSuspensionAnim.awaitEnd()
                        mTomatoBellSuspensionAnim.start()
                        mTomatoBellSuspensionAnim.awaitEnd()
                        ivCalendarSuspension.visibility = View.GONE
                    }
                }
                MainPageIndex.CALENDAR -> {
                    lifecycleScope.launch {
                        ivTomatoBell.isChecked = false
                        ivCalendar.isChecked = true
                        ivCalendarSuspension.visibility = View.VISIBLE
                        mCalendarSuspensionAnim.cancel()
                        mTomatoBellSuspensionAnim.cancel()
                        mTomatoBellSuspensionAnim.reverse()
                        mTomatoBellSuspensionAnim.awaitEnd()
                        mCalendarSuspensionAnim.start()
                    }
                }
            }
        }
        mMainScreenViewModel.mTomatoBellSuspensionButtonResource.observe(viewLifecycleOwner) {
            ivTomatoBellSuspension?.setBackgroundResource(it)
        }
        mMainScreenViewModel.mCalendarSuspensionButtonResource.observe(viewLifecycleOwner) {
            ivCalendarSuspension?.setBackgroundResource(it)
        }
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivTomatoBell -> {
                ivCalendar.expandTouchArea(100)
                mMainScreenViewModel.mMainPageIndex.value = MainPageIndex.TOMATO_BELL
            }
            R.id.ivCalendar -> {
                ivTomatoBell.expandTouchArea(100)
                mMainScreenViewModel.mMainPageIndex.value = MainPageIndex.CALENDAR
            }
        }
    }

    private val mOnMainTomatoBellFragmentCallback = object : OnMainTomatoBellFragmentCallback {
        override fun setOnTouchEventListener(onTouchListener: View.OnTouchListener?) {
            if (ivTomatoBellSuspension != null) {
                ivTomatoBellSuspension.setOnTouchListener(onTouchListener)
            }
        }

        override fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?) {
            if (ivTomatoBellSuspension != null) {
                ivTomatoBellSuspension.setOnLongClickListener(onLongClickListener)
            }
        }

        override fun setOnClickListener(onClickListener: View.OnClickListener?) {
            if (ivTomatoBellSuspension != null) {
                ivTomatoBellSuspension.setOnClickListener(onClickListener)
            }
        }
    }

    private val mOnMainCalendarFragmentCallback = object : OnMainCalendarFragmentCallback {
        override fun setOnClickListener(onClickListener: View.OnClickListener?) {
            if (ivCalendarSuspension != null) {
                ivCalendarSuspension.setOnClickListener(onClickListener)
            }
        }
    }
}
