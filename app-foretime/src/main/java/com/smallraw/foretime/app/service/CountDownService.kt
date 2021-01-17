/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class CountDownService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notifyID = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notifyID)
        } else {
            createErrorNotification(notifyID)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder = CountDownBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class CountDownBinder : Binder() {
        fun getService() = this@CountDownService
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
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .build()
        startForeground(notifyID, notification)
    }
}
