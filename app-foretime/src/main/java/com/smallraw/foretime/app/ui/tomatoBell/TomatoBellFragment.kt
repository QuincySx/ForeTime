package com.smallraw.foretime.app.ui.tomatoBell

import android.os.Bundle
import android.os.SystemClock
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.timer.CountDownManager
import com.smallraw.foretime.app.common.widget.OnClickProgressListener
import com.smallraw.foretime.app.model.CountDownModel
import com.smallraw.foretime.app.ui.main.OnMainActivityCallback
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.utils.ms2Minutes
import kotlinx.android.synthetic.main.fragment_tomato_bell.*
import java.util.concurrent.LinkedBlockingQueue

class TomatoBellFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(onMainActivityCallback: OnMainActivityCallback): TomatoBellFragment {
            val fragment = TomatoBellFragment()
            fragment.onMainActivityCallback = onMainActivityCallback
            return fragment
        }
    }

    private var isDisplay = false
    private var mSuspensionHandleQueue = LinkedBlockingQueue<Int>(1)
    var onMainActivityCallback: OnMainActivityCallback? = null
    private val countDownModel = CountDownModel.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tomato_bell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countDownModel.addOnCountDownListener { state, countdownState, totalTime, lastTime ->
            if (!isAdded) {
                return@addOnCountDownListener
            }
            when (countdownState) {
                CountDownManager.STATE_INIT -> {
                    stateInit(state, totalTime, lastTime)
                }
                CountDownManager.STATE_RUNNING -> {
                    stateRunning(state, totalTime, lastTime)
                }
                CountDownManager.STATE_RUNNING_PAUSE -> {
                    statePause(state, totalTime, lastTime)
                }
                CountDownManager.STATE_RUNNING_FINISH -> {
                    stateFinish(state, totalTime, lastTime)
                }
            }
        }
        countDownModel.init(CountDownModel.WORKING)
        ivSetting.setOnClickListener {
            TomatoSettingDialog(context).showAtViewAuto(it)
        }
    }

    override fun onResume() {
        super.onResume()
        isDisplay = true
    }

    fun showViewAction() {
        isDisplay = true
        changeSuspensionIcon()
        onLongClickListener()
        onMainActivityCallback?.setOnClickListener(View.OnClickListener {
            onClickListener()
        })
    }

    fun hiddenViewAction() {
        isDisplay = false
    }

    /**
     * 修改底部图标
     * TODO 底部图标应该两个叠加,业务操作隔离
     */
    private fun changeSuspensionIcon(resId: Int = -1) {
        if (isDisplay) {
            val res = mSuspensionHandleQueue.poll()
            if (resId == -1) {
                if(res == null){
                    onMainActivityCallback?.onChangeIvSuspension(getSuspensionIcon())
                }else{
                    onMainActivityCallback?.onChangeIvSuspension(res)
                }
            } else {
                onMainActivityCallback?.onChangeIvSuspension(resId)
            }
        } else {
            addSuspensionHandle(resId)
        }
    }

    private fun addSuspensionHandle(resId: Int) {
        if (!mSuspensionHandleQueue.isEmpty()) {
            mSuspensionHandleQueue.poll()
        }
        mSuspensionHandleQueue.add(resId)
    }

    private fun stateInit(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触开始专注")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
            }
            CountDownModel.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("长按取消休息")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
            }
        }
    }

    private fun stateRunning(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("持续专注中")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_pause)
            }
            CountDownModel.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("站起来走一走")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
            }
        }
    }

    private fun statePause(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触继续 长按终止")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
            }
        }
    }

    private fun stateFinish(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("稍后进入休息")
                countDownModel.init(CountDownModel.REPOSE)
                countDownModel.start()
            }
            CountDownModel.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                countDownModel.init(CountDownModel.WORKING)
                setHint("点击继续工作")
            }
        }
    }

    private fun setHint(s: String) {
        viewOperationHints.visibility = View.VISIBLE
        viewOperationHints.text = s
    }

    private fun refreshTimeSchedule(@ColorInt color: Int, totalTime: Long, lastTime: Long) {
        viewTimeSchedule.setProgressColor(color)
        val process = lastTime.toFloat() / totalTime
        viewTimeSchedule.setProgress(process)
        viewTimeSchedule.setText(ms2Minutes(lastTime))
        viewTimeSchedule.postInvalidate()
    }

    private fun onClickListener() {
        when (countDownModel.currentStatus) {
            CountDownModel.WORKING -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {
                        countDownModel.start()
                    }
                    CountDownManager.STATE_RUNNING -> {
                        countDownModel.pause()
                    }
                    CountDownManager.STATE_RUNNING_PAUSE -> {
                        countDownModel.resume()
                    }
                }
            }
            CountDownModel.REPOSE -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {
                        countDownModel.start()
                    }
                }
            }
        }
    }

    /**
     * 获取当前的 Icon
     */
    @DrawableRes
    private fun getSuspensionIcon(): Int {
        return when (countDownModel.currentStatus) {
            CountDownModel.WORKING -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {
                        R.drawable.ic_tab_suspension_start
                    }
                    CountDownManager.STATE_RUNNING -> {
                        R.drawable.ic_tab_suspension_pause
                    }
                    CountDownManager.STATE_RUNNING_PAUSE -> {
                        R.drawable.ic_tab_suspension_start
                    }
                    else -> {
                        R.drawable.ic_tab_suspension_start
                    }
                }
            }
            CountDownModel.REPOSE -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                    CountDownManager.STATE_RUNNING -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                    CountDownManager.STATE_RUNNING_PAUSE -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                    else -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                }
            }
            else -> {
                R.drawable.ic_tab_suspension_start
            }
        }
    }

    private fun onLongClickListener() {
        onMainActivityCallback?.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                return true
            }
        })
        onMainActivityCallback?.setOnTouchEventListener(object : OnClickProgressListener() {
            override fun onStart() {
                var isLongClick = false
                when (countDownModel.currentStatus) {
                    CountDownModel.WORKING -> {
                        when (countDownModel.countDownStatus) {
                            CountDownManager.STATE_RUNNING_PAUSE -> {
                                isLongClick = true
                            }
                            CountDownManager.STATE_RUNNING -> {
                                isLongClick = true
                            }
                        }
                    }
                    CountDownModel.REPOSE -> {
                        when (countDownModel.countDownStatus) {
                            CountDownManager.STATE_RUNNING -> {
                                isLongClick = true
                            }
                        }
                    }
                }
                if (isLongClick) {
                    viewTimeProgress.setProgress(0F)
                    viewTimeProgress.visibility = View.VISIBLE
                }
            }

            override fun onProgress(progress: Double) {
                viewTimeProgress.setProgress(progress.toFloat())
            }

            override fun onSuccess() {
                viewTimeProgress.visibility = View.GONE
                responseEvent()
            }

            override fun onCancel() {
                viewTimeProgress.visibility = View.GONE
            }
        })
    }

    private fun responseEvent() {
        when (countDownModel.currentStatus) {
            CountDownModel.WORKING -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_RUNNING_PAUSE, CountDownManager.STATE_RUNNING -> {
                        countDownModel.stop()
                        countDownModel.init(CountDownModel.REPOSE)
                        countDownModel.start()
                        SystemClock.sleep(2000)
                    }
                }
            }
            CountDownModel.REPOSE -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_RUNNING -> {
                        countDownModel.stop()
                        countDownModel.init(CountDownModel.WORKING)
                    }
                }
            }
        }
    }
}
