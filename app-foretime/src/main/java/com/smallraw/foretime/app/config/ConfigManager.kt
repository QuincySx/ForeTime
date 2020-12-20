package com.smallraw.foretime.app.config

import androidx.annotation.StringDef
import androidx.annotation.WorkerThread
import android.util.Log
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.config.ConfigName.Companion.CalendarConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.MusicConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.TaskConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
import com.smallraw.foretime.app.repository.database.entity.ConfigDO
import kotlin.reflect.full.memberProperties

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
    clazz.declaredFields
            .filter {
                it.name != "\$change"
                        && it.name != "serialVersionUID"
            }
            .forEach {
                val findByKey = configDao.value.findByKey(it.name)
                it.isAccessible = true
                if (findByKey.isNotEmpty() && null != findByKey[0].value) {
                    it.set(newInstance, convert(findByKey[0].value!!, it.type))
                } else {
                    it.set(newInstance, it.get(config))
                }
            }
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
    // Kotlin 反射耗时太久
     val kotlin = configInfo.javaClass.kotlin
     kotlin.memberProperties.forEach {
        Log.e("==反射==", "${it.name} ${it.call(configInfo)} ")
     }
    configInfo.javaClass
            .declaredFields
            .filter {
                it.name != "\$change"
                        && it.name != "serialVersionUID"
            }
            .forEach {
                it.isAccessible = true
                Log.e("==反射==", "${it.name} ${it.get(configInfo)} ")
                configDao.value.insert(ConfigDO(it.name, "${it.get(configInfo)}"))
            }
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
