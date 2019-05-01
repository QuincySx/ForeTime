package com.smallraw.foretime.app.ui.tomatoBell

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
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

    override fun onResume() {
        super.onResume()
        isDisplay = true
        dispatchRefresh()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (null != service) {
            mCountDownService = (service as CountDownService.CountDownBinder).getService()
        }

        dispatchRefresh()

        mCountDownService?.setCountDownTickListener(object : CountDownService.OnCountDownServiceListener {
            override fun onCountDownChange() {
                dispatchRefresh()
            }

            override fun onCountDownTick(millisUntilFinished: Long) {
                dispatchRefresh()
            }

            override fun onCountDownFinish() {
                dispatchRefresh()
            }
        })
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        val intent = Intent(context, CountDownService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        context?.unbindService(this)
        super.onDestroy()
    }

    /**
     * 处理底部按钮点击事件
     */
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
        startCountDownService()
    }

    /**
     * 处理底部按钮长按事件
     */
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
                            CountDownStatus.PAUSE -> {
                                isLongClick = true
                            }
                            CountDownStatus.RUNNING -> {
                                isLongClick = true
                            }
                        }
                    }
                    CountDownType.REPOSE -> {
                        when (status) {
                            CountDownStatus.RUNNING -> {
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

    /**
     * 处理倒计时结束或者长按的事件
     */
    private fun responseEvent() {
        val type = mCountDownService?.getType()
        val status = mCountDownService?.getStatus()

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.PAUSE, CountDownStatus.RUNNING -> {
                        mCountDownService?.reset(CountDownType.REPOSE)
                        mCountDownService?.start()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.RUNNING -> {
                        mCountDownService?.reset(CountDownType.WORKING)
                    }
                }
            }
        }
    }

    /**
     * 显示当前页面
     */
    fun showViewAction() {
        isDisplay = true
        changeSuspensionIcon()
        onLongClickListener()
        onMainActivityCallback?.setOnClickListener(View.OnClickListener {
            onClickListener()
        })
    }

    /**
     * 隐藏当前页面
     */
    fun hiddenViewAction() {
        isDisplay = false
    }

    /**
     * 分发刷新倒计时、按钮、提示等视图的动作
     */
    private fun dispatchRefresh() {
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

    /**
     * 刷新为初始化状态视图
     */
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

    /**
     * 刷新为运行状态视图
     */
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

    /**
     * 刷新为暂停状态视图
     */
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

    /**
     * 刷新为完成状态视图
     */
    private fun stateFinish(state: Int, totalTime: Long, lastTime: Long) {
        when (state) {
            CountDownType.WORKING -> {
                val color = ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("稍后进入休息")
            }
            CountDownType.REPOSE -> {
                val color = ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                refreshTimeSchedule(color, totalTime, lastTime)
                setHint("点击继续工作")
            }
        }
    }

    /**
     * 设置番茄钟提示语
     */
    private fun setHint(hint: String) {
        viewOperationHints.visibility = View.VISIBLE
        viewOperationHints.text = hint
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

    /**
     * 通过队列消除重复事件
     */
    private fun addSuspensionHandle(resId: Int) {
        if (!mSuspensionHandleQueue.isEmpty()) {
            mSuspensionHandleQueue.poll()
        }
        mSuspensionHandleQueue.add(resId)
    }

    /**
     * 刷新倒计时控件 UI
     */
    private fun refreshTimeSchedule(@ColorInt color: Int, totalTime: Long, lastTime: Long) {
        viewTimeSchedule.setProgressColor(color)
        val process = lastTime.toFloat() / totalTime
        viewTimeSchedule.setProgress(process)
        viewTimeSchedule.setText(ms2Minutes(lastTime))
        viewTimeSchedule.postInvalidate()
    }

    /**
     * 根据当前状态获取底部按钮 Icon
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

    /**
     * 启动服务
     */
    fun startCountDownService() {
        context?.apply {
            val intent = Intent(this, CountDownService::class.java)
            ContextCompat.startForegroundService(this, intent)
        }
    }
}
