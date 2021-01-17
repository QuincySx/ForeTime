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
package com.smallraw.foretime.app.config

import android.util.Log
import androidx.annotation.StringDef
import androidx.annotation.WorkerThread
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.config.ConfigName.Companion.CalendarConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.MusicConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.TaskConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
import com.smallraw.foretime.app.repository.database.entity.ConfigDO
import kotlin.system.measureTimeMillis

@StringDef(TaskConfig, CalendarConfig, MusicConfig)
annotation class ConfigName {
    companion object {
        const val TaskConfig = "TaskConfig"
        const val CalendarConfig = "CalendarConfig"
        const val MusicConfig = "MusicConfig"
    }
}

private val configDao = lazy {
    App.getInstance().getDatabase().configDao()
}

@WorkerThread
fun getTaskSettingConfig(): TaskConfigInfo = getConfig(TaskConfig)

@WorkerThread
fun getCalendarSettingConfig(): CalendarConfigInfo = getConfig(CalendarConfig)

@WorkerThread
fun getMusicSettingConfig(): MusicConfigInfo = getConfig(MusicConfig)

private fun <T> getConfig(@ConfigName configName: String): T {
    val clazz = sConfigList[configName]!!.second
    val config = loadAssetsConfig<T>(configName)
    val newInstance = clazz.newInstance()
    val measureTimeMillis = measureTimeMillis {
        clazz.declaredFields
            .filter {
                it.name != "\$change" &&
                    it.name != "serialVersionUID"
            }
            .forEach {
                val findByKey = configDao.value.findByKey(it.name)
                it.isAccessible = true
                if (null != findByKey?.value) {
                    it.set(newInstance, convert(findByKey.value!!, it.type))
                } else {
                    it.set(newInstance, it.get(config))
                }
            }
    }
    Log.e("==Java 反射==", "读取总耗时 $measureTimeMillis ms")
    return newInstance as T
}

@WorkerThread
fun CalendarConfigInfo.saveConfig() {
    save(this)
}

@WorkerThread
fun MusicConfigInfo.saveConfig() {
    save(this)
}

@WorkerThread
fun TaskConfigInfo.saveConfig() {
    save(this)
}

@WorkerThread
private fun save(configInfo: Any) {
    // Kotlin 反射耗时太久 800ms
//    val measureTimeMillis = measureTimeMillis {
//        val kotlin = configInfo.javaClass.kotlin
//        val configDOList = ArrayList<ConfigDO>(kotlin.memberProperties.size)
//        kotlin.memberProperties.forEach {
//            configDOList.add(ConfigDO(it.name, "${it.get(configInfo)}"))
//        }
//        configDao.value.inserts(configDOList)
//    }
//    Log.e("==Kotlin 反射==", "总耗时 $measureTimeMillis ms")

    val measureTimeMillis = measureTimeMillis {
        val fields = configInfo.javaClass
            .declaredFields
            .filter {
                it.name != "\$change" &&
                    it.name != "serialVersionUID"
            }
        val configDOList = ArrayList<ConfigDO>(fields.size)
        fields.forEachIndexed { index, field ->
            field.isAccessible = true
            configDOList.add(ConfigDO(field.name, "${field.get(configInfo)}"))
        }
        configDao.value.inserts(configDOList)
    }
    Log.e("==Java 反射==", "总耗时 $measureTimeMillis ms")
}

private fun convert(param: String, classz: Class<*>): Any {
    return when (classz.name) {
        "string" -> param
        "boolean" -> param.toBoolean()
        "long" -> param.toLong()
        "short" -> param.toShort()
        "int" -> param.toInt()
        "double" -> param.toDouble()
        "float" -> param.toFloat()
        "byte" -> param.toByte()
        "charArray" -> param.toCharArray()
        "bigDecimal" -> param.toBigDecimal()
        else -> param
    }
}
