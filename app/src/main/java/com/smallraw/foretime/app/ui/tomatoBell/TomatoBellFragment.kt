package com.smallraw.foretime.app.ui.tomatoBell

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.timer.CountDownManager
import com.smallraw.foretime.app.common.widget.OnClickProgressListener
import com.smallraw.foretime.app.model.CountDownModel
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.utils.ms2Minutes
import kotlinx.android.synthetic.main.fragment_tomato_bell.*


class TomatoBellFragment : BaseFragment() {
    val countDownModel = CountDownModel.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tomato_bell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewTimeSchedule.setOnClickListener {
            onClickListener()
        }
        onLongClickListener()
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
    }

    private fun stateInit(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触开始专注")
            }
            CountDownModel.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("长按取消休息")
            }
        }
    }

    private fun stateRunning(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("持续专注中")
            }
            CountDownModel.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("站起来走一走")
            }
        }
    }

    private fun statePause(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownModel.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触继续 长按终止")
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

    fun refreshTimeSchedule(@ColorInt color: Int, totalTime: Long, lastTime: Long) {
        viewTimeSchedule.setProgressColor(color)
        val process = lastTime.toFloat() / totalTime
        viewTimeSchedule.setProgress(process)
        viewTimeSchedule.setText(ms2Minutes(lastTime))
        viewTimeSchedule.postInvalidate()
    }

    fun onClickListener() {
        when (countDownModel.curretStatus) {
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
                    CountDownManager.STATE_RUNNING_FINISH -> {

                    }
                }
            }
            CountDownModel.REPOSE -> {
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
                    CountDownManager.STATE_RUNNING_FINISH -> {

                    }
                }
            }
        }
    }

    fun onLongClickListener() {
        viewTimeSchedule.setOnTouchListener(object : OnClickProgressListener() {
            override fun onStart() {
                viewTimeProgress.visibility = View.VISIBLE
            }

            override fun onProgress(progress: Double) {
                viewTimeProgress.setProgress(progress.toFloat())
            }

            override fun onSuccess() {
                viewTimeProgress.visibility = View.GONE
            }

            override fun onCancel() {
                viewTimeProgress.visibility = View.GONE
            }
        })
        when (countDownModel.curretStatus) {
            CountDownModel.WORKING -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {

                    }
                    CountDownManager.STATE_RUNNING -> {

                    }
                    CountDownManager.STATE_RUNNING_PAUSE -> {

                    }
                    CountDownManager.STATE_RUNNING_FINISH -> {

                    }
                }
            }
            CountDownModel.REPOSE -> {
                when (countDownModel.countDownStatus) {
                    CountDownManager.STATE_INIT -> {

                    }
                    CountDownManager.STATE_RUNNING -> {

                    }
                    CountDownManager.STATE_RUNNING_PAUSE -> {

                    }
                    CountDownManager.STATE_RUNNING_FINISH -> {

                    }
                }
            }
        }
    }
}
