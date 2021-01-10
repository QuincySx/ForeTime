package com.smallraw.foretime.app.ui.main.tomatoBell

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.BR
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseFragment
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.common.widget.OnClickProgressListener
import com.smallraw.foretime.app.service.CountDownService
import com.smallraw.foretime.app.tomatoBell.CountDownStatus
import com.smallraw.foretime.app.tomatoBell.CountDownType
import com.smallraw.foretime.app.tomatoBell.TomatoBellKit
import com.smallraw.foretime.app.ui.main.MainScreenViewModel
import com.smallraw.foretime.app.ui.main.OnMainTomatoBellFragmentCallback
import com.smallraw.foretime.app.ui.musicListActivity.MusicListActivity
import com.smallraw.foretime.app.viewmodle.MusicViewModel
import java.util.concurrent.LinkedBlockingQueue

class TomatoBellFragment : BaseFragment(), ServiceConnection {

    companion object {
        @JvmStatic
        fun newInstance(onMainTomatoBellFragmentCallback: OnMainTomatoBellFragmentCallback): TomatoBellFragment {
            val fragment = TomatoBellFragment()
            fragment.onMainTomatoBellFragmentCallback = onMainTomatoBellFragmentCallback
            return fragment
        }
    }

    private var mCountDownService: CountDownService? = null
    private var isDisplay = false
    private var mSuspensionHandleQueue = LinkedBlockingQueue<Int>(1)
    var onMainTomatoBellFragmentCallback: OnMainTomatoBellFragmentCallback? = null
    private val mMainScreenViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainScreenViewModel::class.java)
    }
    private val mTomatoBellKit by lazy {
        TomatoBellKit.getInstance()
    }
    private lateinit var mMusicViewModel: MusicViewModel
    private lateinit var mTomatoBellViewModel: TomatoBellViewModel

    override fun initViewModel() {
        mMusicViewModel = getApplicationScopeViewModel(MusicViewModel::class.java)
        mTomatoBellViewModel = getApplicationScopeViewModel(TomatoBellViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_tomato_bell)
            .addBindingParam(BR.state, mTomatoBellViewModel)
            .addBindingParam(BR.musicVm, mMusicViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val intent = Intent(context, CountDownService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
//        mBinding = FragmentTomatoBellBinding.inflate(inflater, container, false)
//        return mBinding.root
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mBinding.ivSetting.setOnClickListener {
//            context?.let { context ->
//                TomatoSettingDialog.Builder(context)
//                    .setOnChangeListener { mTomatoBellKit.refreshTimeMillis() }
//                    .atViewAuto(it)
//                    .build()
//                    .show()
//            }
//        }
//
//        mBinding.layoutMusic.setOnClickListener {
//            val i = Intent(activity, MusicListActivity::class.java)
//            startActivity(i)
//        }

        mTomatoBellKit.mSurplusTimeMillisLiveData.observe(viewLifecycleOwner) {
            dispatchRefresh()
        }
        mTomatoBellKit.mCountDownStatusLiveData.observe(viewLifecycleOwner) {
            dispatchRefresh()
        }
        mTomatoBellKit.mCountDownTypeLiveData.observe(viewLifecycleOwner) {
            dispatchRefresh()
        }
    }

    inner class ClickProxy() {
        fun onSettingClick(view: View) {
            context?.let { context ->
                TomatoSettingDialog.Builder(context)
                    .setOnChangeListener { mTomatoBellKit.refreshTimeMillis() }
                    .atViewAuto(view)
                    .build()
                    .show()
            }
        }

        fun onMusicClick(view: View) {
            val i = Intent(activity, MusicListActivity::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        isDisplay = true
        dispatchRefresh()

        showViewAction()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        showViewAction()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (null != service) {
            mCountDownService = (service as CountDownService.CountDownBinder).getService()
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        val intent = Intent(context, CountDownService::class.java)
        context?.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        hiddenViewAction()
        super.onPause()
    }

    override fun onDestroy() {
        context?.unbindService(this)
        super.onDestroy()
    }

    /**
     * 处理底部按钮点击事件
     */
    private fun onClickListener() {
        val type = mTomatoBellKit.getType()
        val status = mTomatoBellKit.getStatus()

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.READY -> {
                        mTomatoBellKit.start()
                    }
                    CountDownStatus.RUNNING -> {
                        mTomatoBellKit.pause()
                    }
                    CountDownStatus.PAUSE -> {
                        mTomatoBellKit.resume()
                    }
                    else -> {
                        mTomatoBellKit.start()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.READY -> {
                        mTomatoBellKit.start()
                    }
                }
            }
            else -> {
                mTomatoBellKit.start()
            }
        }
        startCountDownService()
    }

    /**
     * 处理底部按钮长按事件
     */
    private fun onLongClickListener() {
        onMainTomatoBellFragmentCallback?.setOnLongClickListener(View.OnLongClickListener { true })
        onMainTomatoBellFragmentCallback?.setOnTouchEventListener(object :
            OnClickProgressListener() {
            override fun onStart() {
                val type = mTomatoBellKit?.getType()
                val status = mTomatoBellKit?.getStatus()

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
//                    mBinding.viewTimeProgress.setProgress(0F)
//                    mBinding.viewTimeProgress.visibility = View.VISIBLE
                }
            }

            override fun onProgress(progress: Double) {
//                mBinding.viewTimeProgress.setProgress(progress.toFloat())
            }

            override fun onSuccess() {
//                mBinding.viewTimeProgress.visibility = View.GONE
                responseEvent()
            }

            override fun onCancel() {
//                mBinding.viewTimeProgress.visibility = View.GONE
            }
        })
    }

    /**
     * 处理倒计时结束或者长按的事件
     */
    private fun responseEvent() {
        val type = mTomatoBellKit?.getType()
        val status = mTomatoBellKit?.getStatus()

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.PAUSE, CountDownStatus.RUNNING -> {
                        mTomatoBellKit?.reset(CountDownType.REPOSE)
                        mTomatoBellKit?.start()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.RUNNING -> {
                        mTomatoBellKit?.reset(CountDownType.WORKING)
                    }
                }
            }
        }
    }

    /**
     * 显示当前页面
     */
    private fun showViewAction() {
        isDisplay = true
        changeSuspensionIcon()
        onLongClickListener()
        onMainTomatoBellFragmentCallback?.setOnClickListener(View.OnClickListener {
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
            val type = mTomatoBellKit.getType()
            val status = mTomatoBellKit.getStatus()
            val surplusTimeMillis = mTomatoBellKit.getSurplusTimeMillis()
            val implementTimeMillis = mTomatoBellKit.getImplementTimeMillis()

            if (null == type || null == status || null == surplusTimeMillis || null == implementTimeMillis) {
                return@execute
            }

            when (status) {
                CountDownStatus.READY -> {
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
//        mBinding.viewOperationHints.visibility = View.VISIBLE
//        mBinding.viewOperationHints.text = hint
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
                    mMainScreenViewModel.mTomatoBellSuspensionButtonResource.value =
                        getSuspensionIcon()
                } else {
                    mMainScreenViewModel.mTomatoBellSuspensionButtonResource.value = res
                }
            } else {
                mMainScreenViewModel.mTomatoBellSuspensionButtonResource.value = resId
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
//        mBinding.viewTimeSchedule.setProgressColor(color)
        val process = lastTime.toFloat() / totalTime
//        mBinding.viewTimeSchedule.setProgress(process)
//        mBinding.viewTimeSchedule.setText(ms2Minutes(lastTime))
//        mBinding.viewTimeSchedule.postInvalidate()
    }

    /**
     * 根据当前状态获取底部按钮 Icon
     */
    @DrawableRes
    private fun getSuspensionIcon(): Int {
        return when (mTomatoBellKit.getType()) {
            CountDownType.WORKING -> {
                when (mTomatoBellKit.getStatus()) {
                    CountDownStatus.READY -> {
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
                when (mTomatoBellKit.getStatus()) {
                    CountDownStatus.READY -> {
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
