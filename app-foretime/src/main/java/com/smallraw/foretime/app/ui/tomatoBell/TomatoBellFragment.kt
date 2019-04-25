package com.smallraw.foretime.app.ui.tomatoBell

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.business.timer.CountdownState
import com.smallraw.foretime.app.common.widget.OnClickProgressListener
import com.smallraw.foretime.app.service.CountDownService
import com.smallraw.foretime.app.service.CountDownStatus
import com.smallraw.foretime.app.service.CountDownType
import com.smallraw.foretime.app.ui.main.OnMainActivityCallback
import com.smallraw.foretime.app.ui.musicListActivity.MusicListActivity
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.utils.ms2Minutes
import kotlinx.android.synthetic.main.fragment_tomato_bell.*
import java.util.concurrent.LinkedBlockingQueue

class TomatoBellFragment : BaseFragment(), ServiceConnection {

    companion object {
        @JvmStatic
        fun newInstance(onMainActivityCallback: OnMainActivityCallback): TomatoBellFragment {
            val fragment = TomatoBellFragment()
            fragment.onMainActivityCallback = onMainActivityCallback
            return fragment
        }
    }

    private var mCountDownService: CountDownService? = null
    private var isDisplay = false
    private var mSuspensionHandleQueue = LinkedBlockingQueue<Int>(1)
    var onMainActivityCallback: OnMainActivityCallback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val intent = Intent(context, CountDownService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
        return inflater.inflate(R.layout.fragment_tomato_bell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivSetting.setOnClickListener {
            TomatoSettingDialog(context).showAtViewAuto(it)
        }

        layoutMusic.setOnClickListener {
            val i = Intent(activity, MusicListActivity::class.java)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        context?.unbindService(this)
        super.onDestroy()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        val intent = Intent(context, CountDownService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (null != service) {
            mCountDownService = (service as CountDownService.CountDownBinder).getService()
        }

        refresh()

        mCountDownService?.setCountDownTickListener(object : CountDownService.OnCountDownServiceListener {
            override fun onCountDownChange() {
                refresh()
            }

            override fun onCountDownTick(millisUntilFinished: Long) {
                refresh()
            }

            override fun onCountDownFinish() {
                refresh()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        isDisplay = true
        refresh()
    }

    fun showViewAction() {
        isDisplay = true
        changeSuspensionIcon()
        onLongClickListener()
        onMainActivityCallback?.setOnClickListener(View.OnClickListener {
            onClickListener()
        })
    }

    private fun refresh() {
        (context?.applicationContext as App).getAppExecutors().mainThread().execute {
            val type = mCountDownService?.getType()
            val status = mCountDownService?.getStatus()
            val surplusTimeMillis = mCountDownService?.getSurplusTimeMillis()
            val implementTimeMillis = mCountDownService?.getImplementTimeMillis()

            if (null == type || null == status || null == surplusTimeMillis || null == implementTimeMillis) {
                return@execute
            }

            when (status) {
                CountDownStatus.SPARE -> {
                    stateInit(type, implementTimeMillis, surplusTimeMillis)
                }
                CountDownStatus.RUNNING -> {
                    stateRunning(type, implementTimeMillis, surplusTimeMillis)
                }
                CountDownStatus.PAUSE -> {
                    statePause(type, implementTimeMillis, surplusTimeMillis)
                }
                CountDownStatus.FINISH -> {
                    stateFinish(type, implementTimeMillis, surplusTimeMillis)
                }
            }
        }
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
                if (res == null) {
                    onMainActivityCallback?.onChangeIvSuspension(getSuspensionIcon())
                } else {
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
            CountDownType.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触开始专注")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
            }
            CountDownType.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("长按取消休息")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
            }
        }
    }

    private fun stateRunning(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownType.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("持续专注中")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_pause)
            }
            CountDownType.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("站起来走一走")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
            }
        }
    }

    private fun statePause(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownType.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("轻触继续 长按终止")
                changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
            }
        }
    }

    private fun stateFinish(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownType.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("稍后进入休息")
                mCountDownService?.change(CountDownType.REPOSE)
                mCountDownService?.start()
            }
            CountDownType.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                mCountDownService?.change(CountDownType.REPOSE)
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
        val type = mCountDownService?.getType()
        val status = mCountDownService?.getStatus()

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.SPARE -> {
                        mCountDownService?.start()
                    }
                    CountDownStatus.RUNNING -> {
                        mCountDownService?.pause()
                    }
                    CountDownStatus.PAUSE -> {
                        mCountDownService?.resume()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.SPARE -> {
                        mCountDownService?.start()
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
        return when (mCountDownService?.getType()) {
            CountDownType.WORKING -> {
                when (mCountDownService?.getStatus()) {
                    CountDownStatus.SPARE -> {
                        R.drawable.ic_tab_suspension_start
                    }
                    CountDownStatus.RUNNING -> {
                        R.drawable.ic_tab_suspension_pause
                    }
                    CountDownStatus.PAUSE -> {
                        R.drawable.ic_tab_suspension_start
                    }
                    else -> {
                        R.drawable.ic_tab_suspension_start
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (mCountDownService?.getStatus()) {
                    CountDownStatus.SPARE -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                    CountDownStatus.RUNNING -> {
                        R.drawable.ic_tab_suspension_rest
                    }
                    CountDownStatus.PAUSE -> {
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
                val type = mCountDownService?.getType()
                val status = mCountDownService?.getStatus()

                if (null == type || null == status) {
                    return
                }

                var isLongClick = false
                when (type) {
                    CountDownType.WORKING -> {
                        when (status) {
                            CountdownState.STATE_RUNNING_PAUSE -> {
                                isLongClick = true
                            }
                            CountdownState.STATE_RUNNING -> {
                                isLongClick = true
                            }
                        }
                    }
                    CountDownType.REPOSE -> {
                        when (status) {
                            CountdownState.STATE_RUNNING -> {
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
        val type = mCountDownService?.getType()
        val status = mCountDownService?.getStatus()

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountdownState.STATE_RUNNING_PAUSE, CountdownState.STATE_RUNNING -> {
                        mCountDownService?.stop()
                        mCountDownService?.change(CountDownType.REPOSE)
                        mCountDownService?.start()
                        SystemClock.sleep(2000)
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountdownState.STATE_RUNNING -> {
                        mCountDownService?.stop()
                        mCountDownService?.change(CountDownType.WORKING)
                    }
                }
            }
        }
    }
}
