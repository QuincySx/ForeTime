package com.smallraw.foretime.app.service

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.util.Log
import com.smallraw.foretime.app.model.CountDownTick
import com.smallraw.foretime.app.service.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.service.CountDownType.Companion.WORKING


class CountDownService : Service(), CountDownTick.OnCountDownTickListener {
    private var TAG = CountDownService::class.java.simpleName
    private var mType = CountDownType.WORKING
    private var onCountDownServiceListener: OnCountDownServiceListener? = null

    override fun onCountDownTick(millisUntilFinished: Long) {
        if (millisUntilFinished < 1000) {
            Log.d(TAG, "Countdown Residual $millisUntilFinished second")
        }
        onCountDownServiceListener?.onCountDownTick(millisUntilFinished)
    }

    override fun onCountDownFinish() {
        Log.d(TAG, "finish")
        changeType()
        onCountDownServiceListener?.onCountDownFinish()
    }

    /**
     * 修改状态
     */
    private fun changeType() {
        when (mType) {
            WORKING -> {
                reset(REPOSE)
                start()
            }
            REPOSE -> {
                reset(WORKING)
            }
        }
    }

    //刷新间隔
    private val mRefreshIntervalTime: Long = 25

    private var mCountTickTimer = CountDownTick(1000 * 15, this, mRefreshIntervalTime)

    override fun onCreate() {
        super.onCreate()

        val notifyID = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notifyID)
        } else {
            createErrorNotification(notifyID)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder = CountDownBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class CountDownBinder : Binder() {
        fun getService() = this@CountDownService
    }

    fun start() {
        startCountDown()
    }

    fun pause() {
        pauseCountDown()
    }

    fun resume() {
        resumeCountDown()
    }

    fun reset(type: Int) {
        resetCountDown(type)
    }

    fun stop() {
        stopCountDown()
    }

    fun setCountDownTickListener(listener: OnCountDownServiceListener) {
        onCountDownServiceListener = listener
    }

    fun getType() = getCountDownType()

    fun getStatus() = getCountDownStatus()

    fun getCountDownType() = mType

    fun getSurplusTimeMillis() = mCountTickTimer.getSurplusTimeMillis()

    fun getImplementTimeMillis() = mCountTickTimer.getImplementTimeMillis()

    private fun startCountDown() {
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.start()
        Log.d(TAG, "CountDown start")
        onCountDownServiceListener?.onCountDownChange()
    }

    private fun pauseCountDown() {
        mCountTickTimer.pause()
        onCountDownServiceListener?.onCountDownChange()
        Log.d(TAG, "CountDown pause")
    }

    private fun resumeCountDown() {
        mCountTickTimer.resume()
        onCountDownServiceListener?.onCountDownChange()
        Log.d(TAG, "CountDown resume")
    }

    private fun stopCountDown() {
        mCountTickTimer.cancel()
        onCountDownServiceListener?.onCountDownChange()
        Log.d(TAG, "CountDown stop")
    }

    private fun resetCountDown(type: Int) {
        mType = type
        if (mCountTickTimer.isRuning()) {
            mCountTickTimer.pause()
        }
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.reset()
        Log.d(TAG, "CountDown Type reset ")
        printCallStatck()
        onCountDownServiceListener?.onCountDownChange()
    }

    fun printCallStatck() {
        val ex = Throwable()
        val stackElements = ex.stackTrace
        if (stackElements != null) {
            Log.e("Statck", "-----------------------------------")
            for (i in stackElements.indices) {
                val buffer = StringBuffer()
                buffer.append(stackElements[i].className).append("\t")
                buffer.append(stackElements[i].fileName).append("\t")
                buffer.append(stackElements[i].lineNumber).append("\t")
                buffer.append(stackElements[i].methodName)

                Log.e("Statck", buffer.toString())
            }
            Log.e("Statck", "-----------------------------------")
        }
    }

    private fun getCountDownStatus(): Int {
        return if (mCountTickTimer.isPause())
            CountDownStatus.PAUSE
        else if (mCountTickTimer.isRuning())
            CountDownStatus.RUNNING
        else if (mCountTickTimer.isFinish())
            CountDownStatus.FINISH
        else
            CountDownStatus.SPARE
    }

    /**
     * 根据类型获取时间
     */
    private fun getCurrentTypeTime(): Long {
        return when (mType) {
            WORKING -> 15 * 1000
            REPOSE -> 10 * 1000
            else -> 0
        }
    }

    private fun createErrorNotification(notifyID: Int) {
        val notification = Notification.Builder(this).build()
        startForeground(notifyID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notifyID: Int) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 通知渠道的id
        val id = "my_channel_01"
        // 用户可以看到的通知渠道的名字.
        val name = "countDown"
        //         用户可以看到的通知渠道的描述
        val description = "countDownTick"
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(id, name, importance)
        // 配置通知渠道的属性
        mChannel.description = description
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        mChannel.enableLights(false)
//        mChannel.lightColor = Color.RED
        // 设置通知出现时的震动（如果 android 设备支持的话）
        mChannel.enableVibration(false)
//        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        // 最后在notificationmanager中创建该通知渠道 //
        mNotificationManager.createNotificationChannel(mChannel)

        // 通知渠道的id
        val CHANNEL_ID = "my_channel_01"
        // Create a notification and set the notification channel.
        val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("New Message").setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .build()
        startForeground(notifyID, notification)
    }

    interface OnCountDownServiceListener {

        /**
         * CountDownService 时间变化触发的事件
         */
        fun onCountDownTick(millisUntilFinished: Long)

        /**
         * CountDownService 变更任务时触发的事件
         */
        fun onCountDownChange()

        /**
         * CountDownService 完成任务时触发的事件
         */
        fun onCountDownFinish()

    }
}
