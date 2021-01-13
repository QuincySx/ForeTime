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
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
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
import com.smallraw.foretime.app.utils.ms2Minutes
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
        mMusicViewModel = getFragmentScopeViewModel(MusicViewModel::class.java)
        mTomatoBellViewModel = getFragmentScopeViewModel(TomatoBellViewModel::class.java)
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTomatoBellKit.getImplementTimeMillis().observe(viewLifecycleOwner) {
            val minutes = ms2Minutes(mTomatoBellKit.getImplementTimeMillis().value!!)
            mTomatoBellViewModel.progress.set(1F)
            mTomatoBellViewModel.timeWrapper.set(minutes)
        }

        mTomatoBellKit.getSurplusTimeMillis().observe(viewLifecycleOwner) {
            val process =
                mTomatoBellKit.getSurplusTimeMillis().value!!.toFloat() / mTomatoBellKit.getImplementTimeMillis().value!!.toFloat()
            val minutes = ms2Minutes(mTomatoBellKit.getSurplusTimeMillis().value!!)
            mTomatoBellViewModel.progress.set(process)
            mTomatoBellViewModel.timeWrapper.set(minutes)
        }

        mTomatoBellKit.getType().observe(viewLifecycleOwner) {
            val color = when (it) {
                CountDownType.WORKING -> ResourcesCompat.getColor(resources, R.color.WorkingProgessColor, null)
                CountDownType.REPOSE -> ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
                else -> ResourcesCompat.getColor(resources, R.color.RestProgessColor, null)
            }
            mTomatoBellViewModel.progressColor.set(color)
            handleHint()
        }

        mTomatoBellKit.getStatus().observe(viewLifecycleOwner) {
            handleHint()
            judgeAutomaticRest()
        }
    }

    inner class ClickProxy() {
        fun onSettingClick(view: View) {
            context?.let { context ->
                TomatoSettingDialog.Builder(context)
                    .setOnChangeListener {
//                        mTomatoBellKit.refreshTimeMillis()
                    }
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
        mTomatoBellKit.action()
        startCountDownService()
    }

    /**
     * 处理底部按钮长按事件
     */
    private fun onLongClickListener() {
        onMainTomatoBellFragmentCallback?.setOnLongClickListener { true }
        onMainTomatoBellFragmentCallback?.setOnTouchEventListener(object : OnClickProgressListener() {
            override fun onStart() {
                val type = mTomatoBellKit.getType().value
                val status = mTomatoBellKit.getStatus().value

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
                    mTomatoBellViewModel.touchTimeProgress.set(0F)
                    mTomatoBellViewModel.touchTimeProgressVisibility.set(true)
                }
            }

            override fun onProgress(progress: Double) {
                mTomatoBellViewModel.touchTimeProgress.set(progress.toFloat())
            }

            override fun onSuccess() {
                mTomatoBellViewModel.touchTimeProgressVisibility.set(false)
                responseEvent()
            }

            override fun onCancel() {
                mTomatoBellViewModel.touchTimeProgressVisibility.set(false)
            }
        })
    }

    /**
     * 处理倒计时结束或者长按的事件
     */
    private fun responseEvent() {
        mTomatoBellKit.actionLong()
    }

    /**
     * 显示当前页面
     */
    private fun showViewAction() {
        isDisplay = true
        changeSuspensionIcon()
        onLongClickListener()
        onMainTomatoBellFragmentCallback?.setOnClickListener {
            onClickListener()
        }
    }

    /**
     * 隐藏当前页面
     */
    private fun hiddenViewAction() {
        isDisplay = false
    }

    private fun judgeAutomaticRest() {
        val type = mTomatoBellKit.getType().value
        val status = mTomatoBellKit.getStatus().value

        if (null == type || null == status) {
            return
        }

        if (type == CountDownType.WORKING && status == CountDownStatus.FINISH) {
            mTomatoBellKit.nextState()
        }
    }

    /**
     * 设置番茄钟提示语
     */
    private fun setHint(hint: String) {
        mTomatoBellViewModel.operationHints.set(hint)
        mTomatoBellViewModel.operationHintsVisibility.set(true)
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
     * 根据当前状态获取底部按钮 Icon
     */
    @DrawableRes
    private fun getSuspensionIcon(): Int {
        return when (mTomatoBellKit.getType().value) {
            CountDownType.WORKING -> {
                when (mTomatoBellKit.getStatus().value) {
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
                when (mTomatoBellKit.getStatus().value) {
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
     * 处理提示信息，和按钮信息
     */
    private fun handleHint() {
        val type = mTomatoBellKit.getType().value
        val status = mTomatoBellKit.getStatus().value

        if (null == type || null == status) {
            return
        }

        when (status) {
            CountDownStatus.READY -> {
                when (type) {
                    CountDownType.WORKING -> {
                        setHint("轻触开始专注")
                        changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
                    }
                    CountDownType.REPOSE -> {
                        setHint("长按取消休息")
                        changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
                    }
                }
            }
            CountDownStatus.RUNNING -> {
                when (type) {
                    CountDownType.WORKING -> {
                        setHint("持续专注中")
                        changeSuspensionIcon(R.drawable.ic_tab_suspension_pause)
                    }
                    CountDownType.REPOSE -> {
                        setHint("站起来走一走")
                        changeSuspensionIcon(R.drawable.ic_tab_suspension_rest)
                    }
                }
            }
            CountDownStatus.PAUSE -> {
                when (type) {
                    CountDownType.WORKING -> {
                        setHint("轻触继续 长按终止")
                        changeSuspensionIcon(R.drawable.ic_tab_suspension_start)
                    }
                }
            }
            CountDownStatus.FINISH -> {
                when (type) {
                    CountDownType.WORKING -> {
                        setHint("稍后进入休息")
                    }
                    CountDownType.REPOSE -> {
                        setHint("点击继续工作")
                    }
                }
            }
        }
    }

    /**
     * 启动服务
     */
    private fun startCountDownService() {
        context?.apply {
            val intent = Intent(this, CountDownService::class.java)
            ContextCompat.startForegroundService(this, intent)
        }
    }
}
