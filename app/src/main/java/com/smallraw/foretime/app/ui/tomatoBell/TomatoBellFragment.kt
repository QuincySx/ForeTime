package com.smallraw.foretime.app.ui.tomatoBell

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.model.TomatoBellManager
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.utils.ms2Minutes
import kotlinx.android.synthetic.main.fragment_tomato_bell.*

class TomatoBellFragment : BaseFragment() {
    val tomatoBellManager = TomatoBellManager.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tomato_bell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewTimeSchedule.setOnClickListener {
            onClickListener()
        }
        viewTimeSchedule.setOnLongClickListener {
            onLongClickListener()
            return@setOnLongClickListener true
        }
        tomatoBellManager.addCountDownListener { state, totalTime, lastTime ->
            when (state) {
                TomatoBellManager.STATE_INIT -> {
                    val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                    setHint("轻触开始专注")
                }
                TomatoBellManager.STATE_WORKING -> {
                    val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_WORKING_PAUSE -> {
                    val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_WORKING_FINISH -> {
                    val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_REST -> {
                    val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_REST_PAUSE -> {
                    val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_REST_FINISH -> {
                    val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
                TomatoBellManager.STATE_STOP -> {
                    val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                    refreshTimeSchedule(color, totalTime, lastTime)
                }
            }
        }
        tomatoBellManager.init()
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
        viewTimeSchedule.invalidate()
        Log.e("===progress==", "  ${process}")
    }

    fun onClickListener() {
        when (tomatoBellManager.countDownState) {
            TomatoBellManager.STATE_INIT -> {
                tomatoBellManager.startWorking()
            }
            TomatoBellManager.STATE_WORKING -> {
                tomatoBellManager.pauseWorking()
            }
            TomatoBellManager.STATE_WORKING_PAUSE -> {
                tomatoBellManager.resumeWorking()
            }
            TomatoBellManager.STATE_WORKING_FINISH -> {
                tomatoBellManager.startRest()
            }
            TomatoBellManager.STATE_REST -> {
                tomatoBellManager.pauseRest()
            }
            TomatoBellManager.STATE_REST_PAUSE -> {
                tomatoBellManager.resumeRest()
            }
            TomatoBellManager.STATE_REST_FINISH -> {
                tomatoBellManager.init()
            }
        }
    }

    fun onLongClickListener() {

    }
}
